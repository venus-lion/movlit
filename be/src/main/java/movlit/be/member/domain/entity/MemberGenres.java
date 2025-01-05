package movlit.be.member.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGenres {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGenreEntity> memberGenreEntityList = new ArrayList<>();

    public MemberGenres(List<MemberGenreEntity> newMemberGenreEntityList) {
        this.memberGenreEntityList.addAll(newMemberGenreEntityList);
    }

    public void replaceWith(List<MemberGenreEntity> newMemberGenreEntityList) {
        memberGenreEntityList.clear();
        memberGenreEntityList.addAll(newMemberGenreEntityList);
    }

    public List<MemberGenreEntity> getNewUnmodifiedList() {
        return Collections.unmodifiableList(memberGenreEntityList);
    }

}
