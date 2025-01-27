package movlit.be.image.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.application.convertor.ImageConvertor;
import movlit.be.image.domain.entity.ImageEntity;
import movlit.be.image.domain.repository.ImageRepository;
import movlit.be.image.presentation.dto.response.ImageResponse;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.pub_sub.RedisMessagePublisher;
import movlit.be.pub_sub.chatRoom.application.service.GroupChatroomService;
import movlit.be.pub_sub.chatRoom.application.service.dto.ProfileImageUpdatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final GroupChatroomService groupChatroomService;
    private final RedisMessagePublisher redisMessagePublisher;

    private final ApplicationEventPublisher eventPublisher;

    @Value("${aws.s3.bucket.folderName}")
    private String folderName;

    public ImageResponse uploadProfileImage(MemberId memberId, MultipartFile file) {
        ImageEntity imageEntity = ImageConvertor.toImageEntity(s3Service.uploadImage(file, folderName), memberId);
        validateMemberExistsInImage(memberId);
        ImageEntity savedImageEntity = imageRepository.upload(imageEntity);
        // 멤버 정보 update
        MemberEntity member = memberRepository.findEntityById(memberId);
        member.updateProfileImgUrl(savedImageEntity.getUrl());
        memberRepository.saveEntity(member);

        // 프로필 업데이트 이후, 업데이트 이벤트 발행
        eventPublisher.publishEvent(new ProfileImageUpdatedEvent(memberId));
        // 프로필 이미지 업데이트 이후, redis publish -> 원래 이거 할려고 했는데 jpa 영속성 컨텍스트 문제로, 제대로 반영된 최신 멤버정보가 안 옴
//        // 1. 해당 멤버가 속한 모든 그룹채팅방 ID를 조회
//        List<GroupChatroomResponseDto> groupChatroomResponseDtoList = groupChatroomService.fetchMyGroupChatList(memberId);
//
//        for (GroupChatroomResponseDto groupChatroomResponseDto : groupChatroomResponseDtoList){
//            System.out.println("해당 멤버가 속한 그룹채팅방 ID :: " + groupChatroomResponseDto.getGroupChatroomId());
//            System.out.println("해당 멤버가 속한 그룹채팅방 NAME :: " + groupChatroomResponseDto.getRoomName());
//        }
//
//        List<GroupChatroomId> groupChatroomIds = groupChatroomResponseDtoList.stream()
//                .map(GroupChatroomResponseDto::getGroupChatroomId).toList();
//
//        // 2. 각 그룹채팅방 ID에 대해 updateRoomDto 생성 및 메세지 발행
//        groupChatroomIds.forEach(groupChatroomId -> {
//            UpdateRoomDto updateRoomDto = new UpdateRoomDto(
//                    groupChatroomId,
//                    MessageType.GROUP,
//                    EventType.MEMBER_PROFILE_UPDATE,
//                    memberId
//            );
//
//            redisMessagePublisher.updateRoom(updateRoomDto);
//
//            System.out.println("ImageService >>> UpdateRoomDto 토픽 발행 :: " + updateRoomDto.toString());
//        });
        return new ImageResponse(savedImageEntity.getImageId(), savedImageEntity.getUrl());
    }

    private void validateMemberExistsInImage(MemberId memberId) {
        if (imageRepository.existsByMemberId(memberId)) {
            imageRepository.deleteByMemberId(memberId);
        }
    }

    public ImageResponse fetchProfileImage(MemberId memberId) {
        return imageRepository.fetchProfileImageByMemberId(memberId);
    }

}
