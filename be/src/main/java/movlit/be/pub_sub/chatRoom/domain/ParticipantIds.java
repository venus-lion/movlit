package movlit.be.pub_sub.chatRoom.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class ParticipantIds {
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> ids = new ArrayList<>();

    public void addParticipant(String id) {
        if (ids.contains(id)) {
            throw new IllegalArgumentException("Participant already added: " + id);     // 예외 생성 필요
        }
        this.ids.add(id);
    }

    public void removeParticipant(String id) {
        if (!ids.contains(id)) {
            throw new IllegalArgumentException("Participant not found: " + id);     // 예외 생성 필요
        }
        this.ids.remove(id);
    }

    public List<String> getIds() {
        return ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantIds that = (ParticipantIds) o;
        return Objects.equals(ids, that.ids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids);
    }
}
