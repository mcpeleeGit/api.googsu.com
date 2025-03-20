-- 회원 테이블 생성
CREATE TABLE IF NOT EXISTS members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 블로그 테이블 생성
CREATE TABLE IF NOT EXISTS blogs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    member_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- 테스트 회원 데이터 삽입
INSERT INTO members (email, password, name) VALUES
('test@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU3VxqW', '테스트 사용자'),
('admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyU3VxqW', '관리자');

-- 테스트 블로그 데이터 삽입
INSERT INTO blogs (title, content, member_id) VALUES
('Spring Boot로 시작하는 웹 개발', 'Spring Boot는 Java 기반의 웹 애플리케이션 개발을 위한 프레임워크입니다. 이 글에서는 Spring Boot의 기본적인 설정과 사용법에 대해 알아보겠습니다.\n\n## 1. Spring Boot란?\n\nSpring Boot는 Spring 프레임워크를 기반으로 하여 웹 애플리케이션을 빠르게 개발할 수 있도록 도와주는 도구입니다.\n\n## 2. 주요 특징\n\n- 내장 톰캣 서버\n- 자동 설정\n- 독립 실행 가능\n- 프로덕션 준비 기능', 1),
('JPA를 활용한 데이터베이스 설계', 'JPA(Java Persistence API)는 Java 객체와 데이터베이스 테이블 간의 매핑을 자동으로 처리해주는 기술입니다.\n\n## JPA의 장점\n\n1. 객체 지향적인 코드 작성 가능\n2. 데이터베이스 독립성 확보\n3. 생산성 향상\n4. 유지보수 용이성', 1),
('RESTful API 설계 가이드', 'RESTful API는 웹 서비스 설계의 표준이 되었습니다. 이 가이드에서는 RESTful API의 기본 원칙과 모범 사례를 살펴보겠습니다.\n\n### REST의 기본 원칙\n\n1. 클라이언트-서버 아키텍처\n2. 무상태성\n3. 캐시 가능성\n4. 계층화된 시스템', 2),
('Docker와 컨테이너화', 'Docker는 애플리케이션을 컨테이너화하여 배포하고 실행하는 플랫폼입니다.\n\n## Docker의 장점\n\n- 환경 일관성\n- 빠른 배포\n- 리소스 효율성\n- 확장성', 2),
('마이크로서비스 아키텍처', '마이크로서비스는 애플리케이션을 작은 독립적인 서비스로 분리하여 개발하고 배포하는 아키텍처 스타일입니다.\n\n### 마이크로서비스의 특징\n\n1. 독립적인 배포\n2. 기술 스택의 자유도\n3. 확장성\n4. 장애 격리', 1); 