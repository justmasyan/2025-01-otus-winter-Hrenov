package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.LectureMaterial;

import java.util.List;

@Repository
public interface LectureMaterialRepository extends JpaRepository<LectureMaterial,Long> {

    List<LectureMaterial> findAllByLectureId(long lectureId);

}
