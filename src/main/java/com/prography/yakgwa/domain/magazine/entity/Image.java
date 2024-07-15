package com.prography.yakgwa.domain.magazine.entity;

import com.prography.yakgwa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "IMAGE_TABLE")
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "magazine_id")
    private Magazine magazine;
    @Enumerated(STRING)
    private ImageType type;
}
