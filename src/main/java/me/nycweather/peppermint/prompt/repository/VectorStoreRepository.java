package me.nycweather.peppermint.prompt.repository;

import me.nycweather.peppermint.prompt.model.VectorStoreModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VectorStoreRepository extends JpaRepository<VectorStoreModel, Long> {
    @Query(value = "SELECT * FROM vector_storage vs ORDER BY vs.embedding <=> CAST(:vector AS vector) LIMIT :topK", nativeQuery = true)
    List<VectorStoreModel> similaritySearch(@Param("vector") String vector, @Param("topK") int topK);
}
