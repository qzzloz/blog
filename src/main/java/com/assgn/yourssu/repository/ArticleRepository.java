package com.assgn.yourssu.repository;

import com.assgn.yourssu.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
