package movlit.be.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import movlit.be.common.util.ids.BaseId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCrewId;

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
