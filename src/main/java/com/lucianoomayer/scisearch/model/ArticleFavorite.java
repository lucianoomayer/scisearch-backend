package com.lucianoomayer.scisearch.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="article_favorites", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "article_id"})})
@Getter
@NoArgsConstructor
public class ArticleFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="article_id", nullable = false)
    private Article article;

    @CreationTimestamp
    @Column(name="favorite_at",nullable = false, updatable = false)
    private Instant favoriteAt;

    public ArticleFavorite(User user, Article article){
        this.user = user;
        this.article = article;
    }
}
