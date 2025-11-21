package com.lucianoomayer.scisearch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name="favorites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteArticle {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String userId;
    private String articleId;
    private String title;
    private String url;
    private String publicationDate;
    private String source;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp favoriteAt;
}
