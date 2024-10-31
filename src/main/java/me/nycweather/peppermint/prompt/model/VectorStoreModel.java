package me.nycweather.peppermint.prompt.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "vector_storage", indexes = {
//        @Index(name = "idx_vector_storage_embedding", columnList = "embedding"),
})
public class VectorStoreModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String documentName;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String owner;
    private LocalDateTime createdAt;
    @Column
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1536)
    private Double[] embedding;
}
