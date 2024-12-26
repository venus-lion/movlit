package movlit.be.common.aspect;

import jakarta.servlet.http.HttpSession;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class PermissionAspect {

    @Autowired
    private MemberReadService memberReadService;

    @Before("@annotation(checkPermission)")
    public void checkPermission(JoinPoint joinPoint, CheckPermission checkPermission) throws IllegalAccessException {
        // 현재 요청의 HttpSession 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalAccessException("현재 요청에서 HttpSession을 가져올 수 없습니다.");
        }
        HttpSession session = attributes.getRequest().getSession();

        String requiredPermission = checkPermission.value();
        String memberId = (String) session.getAttribute("sessmemberId");
        Member currentMember = memberReadService.findByMemberId(memberId);

        if (!currentMember.getRole().equals(requiredPermission)) {
            throw new SecurityException("권한 부족: " + requiredPermission);
        }
        System.out.println("권한 검증 통과: " + joinPoint.getSignature());
    }

}
