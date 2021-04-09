# 데이터베이스 생성
DROP DATABASE IF EXISTS lamplight;
CREATE DATABASE lamplight;
USE lamplight;

# 요청사항 테이블 생성
CREATE TABLE `order` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    #`head` INT(10) UNSIGNED NOT NULL, #예상인원수(삭제 21.04.07)
    religion CHAR(200) NOT NULL, #종교
    `startDate` DATE NOT NULL, #장례시작일
    `endDate` DATE NOT NULL, #장례종료일
    deceasedName CHAR(30) NOT NULL, #고인 이름
    bereavedName CHAR(30) NOT NULL, #유족 이름
    `body` TEXT NOT NULL, #상세요구사항
    funeralHome CHAR(200) NOT NULL, #장례식장
    `region` CHAR(100) NOT NULL, #장례지역
    `expertId` INT(10) UNSIGNED NOT NULL,
    `clientId` INT(10) UNSIGNED NOT NULL,
    stepLevel SMALLINT(2) UNSIGNED DEFAULT 1 NOT NULL COMMENT '(1=의뢰요청,2=의뢰검토(장례준비중),3=장례진행중,4=장례종료(종료확인요청),5=종료확인(최종종료),6=취소)'
);

# 테스트 의뢰 생성
INSERT INTO `order`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '기독교',
    `startDate` = '2021-04-01 12:12:12',
    `endDate` = '2021-04-03 20:20:20',
    deceasedName = '홍길동',
    bereavedName = '홍길순',
    funeralHome = '대전장례식장',
    `region` = '대전광역시',
    `body` = '기타 요청 사항',
    `expertId` = 1,
    `clientId` = 1;

INSERT INTO `order`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '불교',
    `startDate` = '2021-04-01 12:12:12',
    `endDate` = '2021-04-03 20:20:20',
    deceasedName = '임꺽정',
    bereavedName = '임꺽순',
    funeralHome = '서울장례식장',
    `region` = '서울특별시',
    `body` = '기타 요청 사항',
    `expertId` = 1,
    `clientId` = 2;

INSERT INTO `order`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '기독교',
    `startDate` = '2021-04-05 12:12:12',
    `endDate` = '2021-04-07 20:20:20',
    deceasedName = '김삿갓',
    bereavedName = '김아무개',
    funeralHome = '서울장례식장',
    `region` = '서울특별시',
    `body` = '기타 요청 사항',
    `expertId` = 2,
    `clientId` = 1;
    
 
# 의뢰인회원 테이블 생성
CREATE TABLE `client` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(30) NOT NULL,
    loginPw VARCHAR(100) NOT NULL,
    authKey CHAR(80) NOT NULL,
    `name` CHAR(30) NOT NULL,
    `email` CHAR(100) NOT NULL,
    `cellphoneNo` CHAR(20) NOT NULL,
    `region` CHAR(100) NOT NULL #지역

);

# 로그인 ID로 검색했을 때
ALTER TABLE `client` ADD UNIQUE INDEX (`loginId`);

# authKey 칼럼에 유니크 인덱스 추가
ALTER TABLE `client` ADD UNIQUE INDEX (`authKey`);

# 회원, 테스트 데이터 생성
INSERT INTO `client`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'user1',
    loginPw = 'user1',
    authKey = 'authKey1__1',
    `name` = 'user1',
    `email` = 'user1@user1.com',
    `cellphoneNo` = 01011111111,
    `region` = '대전광역시';

INSERT INTO `client`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'user2',
    loginPw = 'user2',
    authKey = 'authKey1__2',
    `name` = 'user2',
    `email` = 'user2@user2.com',
    `cellphoneNo` = 01022222222,
    `region` = '인천광역시';

INSERT INTO `client`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'user3',
    loginPw = 'user3',
    authKey = 'authKey1__3',
    `name` = 'user3',
    `email` = 'user3@user3.com',
    `cellphoneNo` = 01033333333,
    `region` = '광주광역시';
    
# 지도사회원 테이블 생성
CREATE TABLE `expert` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(30) NOT NULL,
    loginPw VARCHAR(100) NOT NULL,
    authKey CHAR(80) NOT NULL,
    acknowledgment_step SMALLINT(2) UNSIGNED DEFAULT 1 NOT NULL COMMENT '(1=가입대기 2=가입승인 3=가입실패)',
    `name` CHAR(30) NOT NULL,
    `email` CHAR(100) NOT NULL,
    `cellphoneNo` CHAR(20) NOT NULL,
    `region` CHAR(100) NOT NULL, #활동지역
    `license` CHAR(100) NOT NULL,  #자격증    
    `career` TEXT NOT NULL #경력
);

# 로그인 ID로 검색했을 때
ALTER TABLE `expert` ADD UNIQUE INDEX (`loginId`);

# authKey 칼럼에 유니크 인덱스 추가
ALTER TABLE `expert` ADD UNIQUE INDEX (`authKey`);

# 전문가회원, 테스트 데이터 생성
INSERT INTO `expert`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'expert1',
    loginPw = 'expert1',
    authKey = 'authKey2__1',
    `name` = 'expert1',
    `email` = 'expert1@expert1.com',
    `cellphoneNo` = 01011111111,
    `region` = '대전광역시',
    `license` = '장례지도사2급',
    `career` = '3년';

INSERT INTO `expert`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'expert2',
    loginPw = 'expert2',
    authKey = 'authKey2__2',
    `name` = 'expert2',
    `email` = 'expert2@expert2.com',
    `cellphoneNo` = 01022222222,
    `region` = '서울특별시',
    `license` = '장례지도사2급',
    `career` = '5년';

INSERT INTO `expert`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'expert3',
    loginPw = 'expert3',
    authKey = 'authKey2__3',
    `name` = 'expert3',
    `email` = 'expert3@expert3.com',
    `cellphoneNo` = 01033333333,
    `region` = '부산광역시',
    `license` = '장례지도사2급',
    `career` = '1년';
    
INSERT INTO `expert`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'expert4',
    loginPw = 'expert4',
    authKey = 'authKey2__4',
    `name` = 'expert4',
    `email` = 'expert4@expert4.com',
    `cellphoneNo` = 01044444444,
    `region` = '대전광역시',
    `license` = '장례지도사2급',
    `career` = '1년';
    
# 도우미회원 테이블 생성
CREATE TABLE `assistant` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(30) NOT NULL,
    loginPw VARCHAR(100) NOT NULL,
    authKey CHAR(80) NOT NULL,
    `name` CHAR(30) NOT NULL,
    `email` CHAR(100) NOT NULL,
    `cellphoneNo` CHAR(20) NOT NULL,
    `region` CHAR(100) NOT NULL, #지역
    `career` TEXT #경력

);

# 로그인 ID로 검색했을 때
ALTER TABLE `assistant` ADD UNIQUE INDEX (`loginId`);

# authKey 칼럼에 유니크 인덱스 추가
ALTER TABLE `assistant` ADD UNIQUE INDEX (`authKey`);

# 도우미회원, 테스트 데이터 생성
INSERT INTO `assistant`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'asst1',
    loginPw = 'asst1',
    authKey = 'authKey3__1',
    `name` = 'asst1',
    `email` = 'asst1@asst1.com',
    `cellphoneNo` = 01011111111,
    `region` = '대전광역시',
    `career` = '5년';

INSERT INTO `assistant`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'asst2',
    loginPw = 'asst2',
    authKey = 'authKey3__2',
    `name` = 'asst2',
    `email` = 'asst2@asst2.com',
    `cellphoneNo` = 01022222222,
    `region` = '인천광역시',
    `career` = '2년';

INSERT INTO `assistant`
SET regDate = NOW(),
    updateDate = NOW(),
    loginId = 'asst3',
    loginPw = 'asst3',
    authKey = 'authKey3__3',
    `name` = 'asst3',
    `email` = 'asst3@asst3.com',
    `cellphoneNo` = 01033333333,
    `region` = '광주광역시',
    `career` = '4년';

# 파일 테이블 추가
CREATE TABLE genFile (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT, # 번호
  regDate DATETIME DEFAULT NULL, # 작성날짜
  updateDate DATETIME DEFAULT NULL, # 갱신날짜
  delDate DATETIME DEFAULT NULL, # 삭제날짜
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, # 삭제상태(0:미삭제,1:삭제)
  relTypeCode CHAR(50) NOT NULL, # 관련 데이터 타입(article, member)
  relId INT(10) UNSIGNED NOT NULL, # 관련 데이터 번호
  originFileName VARCHAR(100) NOT NULL, # 업로드 당시의 파일이름
  fileExt CHAR(10) NOT NULL, # 확장자
  typeCode CHAR(20) NOT NULL, # 종류코드 (common)
  type2Code CHAR(20) NOT NULL, # 종류2코드 (attatchment)
  fileSize INT(10) UNSIGNED NOT NULL, # 파일의 사이즈(byte)
  fileExtTypeCode CHAR(10) NOT NULL, # 파일규격코드(img, video)
  fileExtType2Code CHAR(10) NOT NULL, # 파일규격2코드(jpg, mp4)
  fileNo SMALLINT(2) UNSIGNED NOT NULL, # 파일번호 (1)
  fileDir CHAR(20) NOT NULL, # 파일이 저장되는 폴더명
  PRIMARY KEY (id),
  KEY relId (relId,relTypeCode,typeCode,type2Code,fileNo)
); 


# 리뷰 테이블 추가
CREATE TABLE review (
  id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  `relTypeCode` CHAR(20) NOT NULL,
  relId INT(10) UNSIGNED NOT NULL,
  clientId INT(10) UNSIGNED NOT NULL,
  `body` TEXT NOT NULL
);

# 고속 검색을 위해서 인덱스 걸기
ALTER TABLE review ADD KEY (relTypeCode, relId); 
# SELECT * FROM reply WHERE relTypeCode = 'article' AND relId = 5; # O
# SELECT * FROM reply WHERE relTypeCode = 'article'; # O
# SELECT * FROM reply WHERE relId = 5 AND relTypeCode = 'article'; # X


# 평점 테이블 추가
CREATE TABLE rating (
  id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  `relTypeCode` CHAR(20) NOT NULL,
  relId INT(10) UNSIGNED NOT NULL,
  clientId INT(10) UNSIGNED NOT NULL,
  `point` FLOAT(10)
);

# 고속 검색을 위해서 인덱스 걸기
ALTER TABLE rating ADD KEY (relTypeCode, relId); 
# SELECT * FROM reply WHERE relTypeCode = 'article' AND relId = 5; # O
# SELECT * FROM reply WHERE relTypeCode = 'article'; # O
# SELECT * FROM reply WHERE relId = 5 AND relTypeCode = 'article'; # X

# 장례 테이블 생성
CREATE TABLE `funeral` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    #`head` INT(10) UNSIGNED NOT NULL, #예상인원수
    religion CHAR(200) NOT NULL, #종교
    `startDate` DATE NOT NULL, #장례시작일
    `endDate` DATE NOT NULL, #장례종료일
    deceasedName CHAR(30) NOT NULL, #고인 이름
    bereavedName CHAR(30) NOT NULL, #유족 이름
    `body` TEXT NOT NULL, #상세요구사항
    funeralHome CHAR(200) NOT NULL, #장례식장
    `region` CHAR(100) NOT NULL, #장례지역
    `expertId` INT(10) UNSIGNED NOT NULL,
    `clientId` INT(10) UNSIGNED NOT NULL,
    stepLevel SMALLINT(2) UNSIGNED DEFAULT 2 NOT NULL COMMENT '(2=의뢰승인(장례진행중),3=장례종료(종료확인요청),4=종료확인(최종종료))'
);

# 테스트 장례 생성
INSERT INTO `funeral`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '기독교',
    `startDate` = '2021-04-01 12:12:12',
    `endDate` = '2021-04-03 20:20:20',
    deceasedName = '홍길동',
    bereavedName = '홍길순',
    funeralHome = '대전장례식장',
    `region` = '대전광역시',
    `body` = '기타 요청 사항',
    `expertId` = 1,
    `clientId` = 1;

INSERT INTO `funeral`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '불교',
    `startDate` = '2021-04-01 12:12:12',
    `endDate` = '2021-04-03 20:20:20',
    deceasedName = '임꺽정',
    bereavedName = '임꺽순',
    funeralHome = '서울장례식장',
    `region` = '서울특별시',
    `body` = '기타 요청 사항',
    `expertId` = 1,
    `clientId` = 2;

INSERT INTO `funeral`
SET regDate = NOW(),
    updateDate = NOW(),
    religion = '기독교',
    `startDate` = '2021-04-05 12:12:12',
    `endDate` = '2021-04-07 20:20:20',
    deceasedName = '김삿갓',
    bereavedName = '김아무개',
    funeralHome = '서울장례식장',
    `region` = '서울특별시',
    `body` = '기타 요청 사항',
    `expertId` = 2,
    `clientId` = 1;
    
# 장례와 관련된 도우미 그룹 테이블 생성
CREATE TABLE `funeralRelAssts`(
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    funeralId INT(10) UNSIGNED NOT NULL,
    assistantId INT(10) UNSIGNED NOT NULL
);

