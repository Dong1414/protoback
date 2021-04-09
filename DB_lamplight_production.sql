
SELECT * FROM `order`;
SELECT * FROM `funeral`;
SELECT * FROM `funeralRelAssts`;
SELECT * FROM `client`;
SELECT * FROM `expert`;
SELECT * FROM `assistant`;
SELECT * FROM `genFile`;
SELECT * FROM `review`;
SELECT * FROM `rating`;

TRUNCATE `genFile`;
TRUNCATE `review`;
TRUNCATE `rating`;
TRUNCATE `funeralRelAssts`;

# 게시물 랜덤생성 쿼리
INSERT INTO article
(regDate, updateDate, memberId, boardId, title, `body`)
SELECT NOW(), NOW(), FLOOR(RAND() * 2) + 1, FLOOR(RAND() * 2) + 1, CONCAT('제목_', FLOOR(RAND() * 1000) + 1), CONCAT('내용_', FLOOR(RAND() * 1000) + 1)
FROM article;

INSERT INTO rating
SET regDate = NOW(),
    updateDate = NOW(),
    `relTypeCode` = 'expert',
    relId = 2,
    clientId = 2,
    `point` = 3.5;
    
INSERT INTO rating
SET regDate = NOW(),
    updateDate = NOW(),
    `relTypeCode` = 'expert',
    relId = 2,
    clientId = 3,
    `point` = 3.5;
    
INSERT INTO rating
SET regDate = NOW(),
    updateDate = NOW(),
    `relTypeCode` = 'expert',
    relId = 2,
    clientId = 1,
    `point` = 3.5;
    

# 평점 포인트 포함해서 지도사 리스팅
SELECT E.*,
IF(
(ROUND(AVG(R.point),1)-(ROUND(AVG(R.point),1)-0.5)) > 0.5,
 ROUND(AVG(R.point)),
 ROUND(AVG(R.point))-0.5
) AS extra__ratingPoint
FROM `expert` AS E
LEFT JOIN rating AS R
ON R.relTypeCode = 'expert'
AND E.id = R.relId
GROUP BY E.id
ORDER BY E.id DESC


TRUNCATE funeralRelAssts;

INSERT INTO funeralRelAssts
SET funeralId=1,
assistantId = 1;

INSERT INTO funeralRelAssts
SET funeralId=1,
assistantId = 2;

INSERT INTO funeralRelAssts
SET funeralId=1,
assistantId = 3;

SELECT funeralId 
FROM funeralRelAssts
WHERE assistantId = 1;

# 도우미가 진행중인 장례 리스팅
SELECT F.*,
IFNULL(C.name, "탈퇴회원") AS extra__clientName,
IFNULL(E.name, "탈퇴회원") AS extra__expertName
FROM `funeral` AS F
LEFT JOIN `client` AS C
ON F.clientId = C.id
LEFT JOIN `expert` AS E
ON F.expertId = E.id
WHERE F.id = (
SELECT funeralId 
FROM funeralRelAssts
WHERE assistantId = 1 AND funeralId = F.id
)
ORDER BY F.id DESC

# 지도사가 진행중인 장례 리스팅
SELECT F.*,
IFNULL(C.name, "탈퇴회원") AS extra__clientName,
IFNULL(E.name, "탈퇴회원") AS extra__expertName
FROM `funeral` AS F
LEFT JOIN `client` AS C
ON F.clientId = C.id
LEFT JOIN `expert` AS E
ON F.expertId = E.id
WHERE F.expertId = 1
ORDER BY F.id DESC