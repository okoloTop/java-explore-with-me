package ru.practicum.evm.event.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.evm.category.model.Category;
import ru.practicum.evm.event.enums.EventState;
import ru.practicum.evm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@Getter
@Setter
@Table(name = "events", schema = "public")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "annotation", length = 2000)
    private String annotation;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @Builder.Default
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "description", length = 7000)
    private String description;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Builder.Default
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration = true;

    @Builder.Default
    @Enumerated(STRING)
    @Column(name = "state", nullable = false)
    private EventState state = EventState.PENDING;

    @Column(name = "title")
    private String title;

    @Column(name = "views")
    private Long views;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        var event = (Event) o;
        return getId() != null && Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
