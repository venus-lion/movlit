package movlit.be.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import movlit.be.common.util.ids.BaseId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.ChatroomId;
import movlit.be.common.util.ids.ImageId;
import movlit.be.common.util.ids.MemberGenreId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MemberRChatroomId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeCountId;
import movlit.be.common.util.ids.MovieCommentLikeId;
import movlit.be.common.util.ids.MovieCrewId;
import movlit.be.common.util.ids.MovieHeartCountId;
import movlit.be.common.util.ids.MovieHeartId;

public class IdFactory {

    private IdFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static MemberId createMemberId(String id) {
        return createId(MemberId.class, id);
    }

    public static MemberId createMemberId() {
        return createId(MemberId.class);
    }

    public static BookId createBookId(String id) {
        return createId(BookId.class, id);
    }

    public static BookId createBookId() {
        return createId(BookId.class);
    }

    public static MovieCommentId createMovieCommentId(String id) {
        return createId(MovieCommentId.class, id);
    }

    public static MovieCommentId createMovieCommentId() {
        return createId(MovieCommentId.class);
    }

    public static MovieCrewId createMovieCrewId(String id) {
        return createId(MovieCrewId.class, id);
    }

    public static MovieCrewId createMovieCrewId() {
        return createId(MovieCrewId.class);
    }

    public static MovieHeartId createMovieHeartId(String id) {
        return createId(MovieHeartId.class, id);
    }

    public static MovieHeartId createMovieHeartId() {
        return createId(MovieHeartId.class);
    }

    public static MovieHeartCountId createMovieHeartCountId(String id) {
        return createId(MovieHeartCountId.class, id);
    }

    public static MovieHeartCountId createMovieHeartCountId() {
        return createId(MovieHeartCountId.class);
    }

    public static MovieCommentLikeId createMovieCommentLikeId(String id) {
        return createId(MovieCommentLikeId.class, id);
    }

    public static MovieCommentLikeId createMovieCommentLikeId() {
        return createId(MovieCommentLikeId.class);
    }

    public static MovieCommentLikeCountId createMovieCommentLikeCountId(String id) {
        return createId(MovieCommentLikeCountId.class, id);
    }

    public static MovieCommentLikeCountId createMovieCommentLikeCountId() {
        return createId(MovieCommentLikeCountId.class);
    }

    public static ImageId createImageId(String id) {
        return createId(ImageId.class, id);
    }

    public static ImageId createImageId() {
        return createId(ImageId.class);
    }

    public static MemberGenreId createMemberGenreId(String id) {
        return createId(MemberGenreId.class, id);
    }

    public static MemberGenreId createMemberGenreId() {
        return createId(MemberGenreId.class);
    }

    public static ChatroomId createChatroomId(String id) {
        return createId(ChatroomId.class, id);
    }

    public static ChatroomId createChatroomId() {
        return createId(ChatroomId.class);
    }

    public static MemberRChatroomId createMemberRChatroom(String id) {
        return createId(MemberRChatroomId.class, id);
    }

    public static MemberRChatroomId createMemberRChatroom() {
        return createId(MemberRChatroomId.class);
    }

    private static <T extends BaseId> T createId(Class<T> idClass, String id) {
        try {
            Constructor<T> constructor = idClass.getConstructor(String.class);
            return constructor.newInstance(id);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new IllegalArgumentException("Id 클래스 생성에 실패했습니다.");
        }
    }

    private static <T extends BaseId> T createId(Class<T> idClass) {
        try {
            Constructor<T> constructor = idClass.getConstructor(String.class);
            return constructor.newInstance(IdGenerator.generate());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new IllegalArgumentException("Id 클래스 생성에 실패했습니다.");
        }
    }

}
