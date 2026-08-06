package com.soohyeon.booklog.init;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;

import java.util.List;

/**
 * 80권 더미 파일
 */
public class BookDummyData {
    public static List<Book> books() {
        return List.of(

                new Book("노생거 사원", "제인 오스틴", BookStatus.WISH, 4,
                        "고전 로맨스 소설",
                        "오스틴 작품을 순서대로 읽어보고 싶다."),

                new Book("최소한의 세계사", "이다지", BookStatus.READING, 5,
                        "세계사를 쉽게 이해할 수 있도록 정리한 입문서",
                        "퇴근 후 조금씩 읽는 중."),

                new Book("이방인", "알베르 카뮈", BookStatus.DONE, 5,
                        "부조리와 인간 존재를 다룬 고전",
                        "왜 명작인지 이해할 수 있었다."),

                new Book("마법천자문", "올댓스토리", BookStatus.WISH, 3,
                        "한자를 재미있게 배울 수 있는 학습만화",
                        ""),

                new Book("체호프 단편선", "안톤 체호프", BookStatus.READING, 5,
                        "러시아 문학의 대표 단편집",
                        "짧지만 여운이 크다."),

                new Book("죽음의 수용소에서", "빅터 프랭클", BookStatus.DONE, 5,
                        "극한의 상황에서도 삶의 의미를 찾는 이야기",
                        "삶을 바라보는 관점이 달라졌다."),

                new Book("급류", "정대건", BookStatus.DONE, 5,
                        "인간관계와 감정을 섬세하게 그린 소설",
                        "최근 읽은 소설 중 가장 몰입감 있었다."),

                new Book("두고 온 여름", "성해나", BookStatus.WISH, 4,
                        "여름의 기억을 담은 소설",
                        ""),

                new Book("단 한 사람", "최진영", BookStatus.READING, 5,
                        "사랑과 상실을 담은 장편소설",
                        "구의 증명도 같이 읽어볼 예정이다."),

                new Book("단어가 품은 세계", "황선엽", BookStatus.WISH, 4,
                        "언어와 단어의 의미를 탐구하는 책",
                        ""),

                new Book("쓰가루", "다자이 오사무", BookStatus.WISH, 4,
                        "고향을 여행하며 자신을 돌아보는 에세이",
                        ""),

                new Book("맥베스", "윌리엄 셰익스피어", BookStatus.READING, 5,
                        "셰익스피어의 4대 비극",
                        "희곡이라 읽는 속도가 느리다."),

                new Book("괴테는 모든 것을 말했다", "스즈키 유이", BookStatus.WISH, 4,
                        "문학과 삶을 연결하는 소설",
                        ""),

                new Book("부처님 말씀대로 살아보니", "토니 페르난도", BookStatus.DONE, 4,
                        "불교 철학을 현대적으로 풀어낸 책",
                        "가볍게 읽기 좋았다."),

                new Book("나의 친구들", "프레드릭 배크만", BookStatus.WISH, 5,
                        "우정과 삶을 그린 소설",
                        ""),

                new Book("얼굴들", "이동원", BookStatus.READING, 4,
                        "인간의 다양한 얼굴을 조명한 작품",
                        ""),

                new Book("여름", "이디스 워튼", BookStatus.WISH, 4,
                        "사랑과 자유를 다룬 고전",
                        ""),

                new Book("스프링 부트 3 백엔드 개발자 되기(자바 편)", "신선영", BookStatus.READING, 5,
                        "Spring Boot 기반 백엔드 개발 입문",
                        "프로젝트 진행하면서 참고 중."),

                new Book("죽은 왕녀를 위한 파반느", "박민규", BookStatus.WISH, 5,
                        "독특한 문체가 인상적인 장편소설",
                        ""),

                new Book("세상 친절한 경제상식", "토리텔러", BookStatus.DONE, 4,
                        "경제 기초를 쉽게 설명한 책",
                        "경제 입문용으로 추천할 만하다."),

                new Book("따박따박 경제상식 [ETF 첫걸음]", "밀리의서재 편집부", BookStatus.WISH, 4,
                        "ETF를 쉽게 이해할 수 있는 경제 입문서",
                        ""),

                new Book("파과", "구병모", BookStatus.READING, 5,
                        "강렬한 설정과 문체가 인상적인 장편소설",
                        "결말이 궁금해서 계속 읽게 된다."),

                new Book("행동경제학", "리처드 탈러", BookStatus.WISH, 5,
                        "인간의 의사결정을 경제학적으로 분석한 책",
                        ""),

                new Book("최소한의 삼국지", "최태성", BookStatus.DONE, 4,
                        "삼국지를 쉽고 재미있게 정리한 역사서",
                        "입문용으로 추천할 만하다."),

                new Book("2025 제16회 젊은작가상 수상작품집", "백온유 외", BookStatus.READING, 5,
                        "현대 한국문학 단편 모음집",
                        "작가마다 분위기가 달라 재미있다."),

                new Book("바깥은 여름", "김애란", BookStatus.DONE, 5,
                        "상실과 관계를 섬세하게 그린 단편집",
                        "담백하지만 깊은 여운이 남는다."),

                new Book("석류의 씨", "이디스 워튼", BookStatus.WISH, 4,
                        "인간 심리를 다룬 고전 단편",
                        ""),

                new Book("트로피컬 나이트", "조예은", BookStatus.READING, 4,
                        "미스터리와 공포가 어우러진 소설집",
                        "한 편씩 읽기 좋다."),

                new Book("곰탕", "김영탁", BookStatus.WISH, 4,
                        "한국형 미스터리 장편소설",
                        ""),

                new Book("젊은 ADHD의 슬픔", "정지음", BookStatus.DONE, 5,
                        "ADHD 경험을 솔직하게 풀어낸 에세이",
                        "공감되는 내용이 많았다."),

                new Book("어른의 어휘 공부", "신효원", BookStatus.READING, 4,
                        "더 나은 표현을 위한 어휘 공부",
                        "하루에 조금씩 읽기 좋다."),

                new Book("나태한 완벽주의자", "피터 홀린스", BookStatus.WISH, 4,
                        "완벽주의를 극복하는 방법을 다룬 자기계발서",
                        ""),

                new Book("언러키 스타트업", "정지음", BookStatus.WISH, 4,
                        "스타트업에서의 경험을 담은 에세이",
                        ""),

                new Book("우리가 명함이 없지 일을 안 했냐", "경향신문 젠더기획팀", BookStatus.READING, 5,
                        "다양한 여성 노동의 이야기를 담은 책",
                        "생각할 거리를 많이 던져준다."),

                new Book("여름의 귤을 좋아하세요", "이희영", BookStatus.WISH, 4,
                        "청춘과 성장에 관한 소설",
                        ""),

                new Book("넛지: 파이널 에디션", "리처드 H. 탈러", BookStatus.DONE, 5,
                        "행동경제학의 대표작",
                        "행동경제학을 이해하는 데 큰 도움이 되었다."),

                new Book("우리는 사랑의 얼굴을 가졌고", "김수정", BookStatus.WISH, 4,
                        "사랑을 다양한 시선으로 바라본 에세이",
                        ""),

                new Book("슬픔을 공부하는 슬픔", "신형철", BookStatus.DONE, 5,
                        "문학을 통해 슬픔을 사유하는 평론집",
                        "문장이 아름다워 여러 번 읽고 싶다."),

                new Book("이끼숲", "천선란", BookStatus.READING, 5,
                        "천선란 특유의 따뜻한 SF 감성이 담긴 작품",
                        "분위기가 정말 좋다."),

                new Book("물고기는 존재하지 않는다", "룰루 밀러", BookStatus.DONE, 5,
                        "과학과 철학을 넘나드는 논픽션",
                        "왜 베스트셀러인지 알 것 같다."),

                new Book("홍학의 자리", "정해연", BookStatus.READING, 5,
                        "예측하기 어려운 반전이 매력적인 심리 스릴러",
                        "초반부터 몰입감이 뛰어나다."),

                new Book("자연에 이름 붙이기", "캐럴 계숙 윤", BookStatus.WISH, 4,
                        "자연과 생물 분류의 역사를 다룬 논픽션",
                        ""),

                new Book("초역 부처의 말", "코이케 류노스케", BookStatus.DONE, 5,
                        "현대적으로 재해석한 부처의 가르침",
                        "짧은 문장 하나하나가 깊은 울림을 준다."),

                new Book("제노사이드", "다카노 가즈아키", BookStatus.DONE, 5,
                        "압도적인 스케일의 SF 스릴러",
                        "시간 가는 줄 모르고 읽었다."),

                new Book("나는 왜 무기력을 되풀이하는가", "에리히 프롬", BookStatus.WISH, 5,
                        "무기력의 원인과 인간 심리를 탐구한 책",
                        ""),

                new Book("이기적 유전자", "리처드 도킨스", BookStatus.READING, 5,
                        "진화론을 이해하기 위한 필독서",
                        "천천히 읽으며 정리 중이다."),

                new Book("구의 증명", "최진영", BookStatus.DONE, 5,
                        "사랑과 상실을 깊이 있게 그려낸 소설",
                        "읽고 난 뒤 한동안 여운이 남았다."),

                new Book("날개", "이상", BookStatus.WISH, 4,
                        "한국 현대문학의 대표 단편",
                        ""),

                new Book("사랑의 기술", "에리히 프롬", BookStatus.READING, 5,
                        "사랑을 기술이자 능력으로 바라본 고전",
                        "왜 오래 사랑받는 책인지 알 것 같다."),

                new Book("최소한의 한국사", "최태성", BookStatus.DONE, 4,
                        "한국사를 쉽고 재미있게 정리한 입문서",
                        "흐름 위주로 이해하기 좋았다."),

                new Book("왜 나는 너를 사랑하는가", "알랭 드 보통", BookStatus.WISH, 5,
                        "연애와 사랑의 심리를 철학적으로 풀어낸 에세이",
                        ""),

                new Book("계절은 서두르지 않는다", "김산들", BookStatus.READING, 4,
                        "계절과 삶을 담담하게 풀어낸 에세이",
                        "잠들기 전에 읽기 좋다."),

                new Book("파쇄", "구병모", BookStatus.WISH, 4,
                        "인간의 본성과 관계를 그린 장편소설",
                        ""),

                new Book("삼체 도슨트북", "류츠신", BookStatus.DONE, 4,
                        "『삼체』를 이해하기 위한 해설서",
                        "본편을 읽기 전에 도움이 되었다."),

                new Book("호르몬 체인지", "최정화", BookStatus.WISH, 4,
                        "호르몬과 건강에 대해 쉽게 설명한 책",
                        ""),

                new Book("일억 번째 여름", "청예", BookStatus.READING, 5,
                        "감성적인 분위기의 장편소설",
                        "문체가 아름답다."),

                new Book("안녕이라 그랬어", "김애란", BookStatus.WISH, 5,
                        "김애란의 신작 소설",
                        ""),

                new Book("지뢰 글리코", "아오사키 유고", BookStatus.READING, 5,
                        "독특한 설정의 미스터리 소설",
                        "다음 전개가 계속 궁금하다."),

                new Book("오래된 세계의 농담", "이다혜", BookStatus.DONE, 4,
                        "영화와 문화를 함께 이야기하는 에세이",
                        "부담 없이 읽기 좋은 책이었다."),

                new Book("바스커빌가의 사냥개", "아서 코넌 도일", BookStatus.WISH, 5,
                        "셜록 홈즈 시리즈의 대표작",
                        "추리소설을 좋아해서 꼭 읽어보고 싶다."),

                new Book("데카메론 1", "조반니 보카치오", BookStatus.WISH, 4,
                        "르네상스를 대표하는 고전 단편집",
                        ""),

                new Book("뇌는 어떻게 나를 조종하는가", "크리스 나이바우어", BookStatus.READING, 5,
                        "뇌와 자아의 관계를 쉽게 설명한 심리학 교양서",
                        "생각보다 어렵지 않아 재미있게 읽는 중이다."),

                new Book("프로이트의 감정수업", "강이안", BookStatus.DONE, 4,
                        "프로이트의 이론을 쉽게 풀어낸 심리학 입문서",
                        "프로이트를 처음 접하기에 괜찮았다."),

                new Book("아무도 오지 않는 곳에서", "천선란", BookStatus.WISH, 5,
                        "천선란 특유의 따뜻한 SF 감성이 담긴 소설",
                        ""),

                new Book("저소비 생활", "가제노타미", BookStatus.READING, 4,
                        "불필요한 소비를 줄이는 생활 습관을 소개한 책",
                        "실천해보고 싶은 내용이 많다."),

                new Book("요즘 바이브 코딩 클로드 코드 완벽 가이드", "최지호(코드팩토리)", BookStatus.WISH, 5,
                        "AI를 활용한 바이브 코딩 입문서",
                        ""),

                new Book("강하고 아름다운 할머니가 되고 싶어", "김슬기", BookStatus.DONE, 5,
                        "나이 듦과 삶을 따뜻하게 바라본 에세이",
                        "편안한 마음으로 읽기 좋았다."),

                new Book("장미와 나이프", "히가시노 게이고", BookStatus.READING, 5,
                        "인간 심리를 파고드는 미스터리 소설",
                        "역시 히가시노 게이고답다."),

                new Book("청춘의 독서", "유시민", BookStatus.DONE, 5,
                        "고전을 통해 삶을 돌아보게 하는 독서 에세이",
                        "소개된 책들도 읽어보고 싶어졌다."),

                new Book("내게 남은 스물다섯 번의 계절", "슈테판 셰퍼", BookStatus.WISH, 4,
                        "삶과 시간을 돌아보게 하는 소설",
                        ""),

                new Book("흰 고래의 흼에 대하여", "홍한별", BookStatus.READING, 4,
                        "번역가의 시선으로 바라본 언어와 세계",
                        "번역이라는 일이 새롭게 보인다."),

                new Book("네버 라이", "프리다 맥파든", BookStatus.DONE, 5,
                        "반전이 뛰어난 심리 스릴러",
                        "결말이 정말 예상 밖이었다."),

                new Book("폴링 인 폴", "백수린", BookStatus.WISH, 4,
                        "계절과 사람을 섬세하게 그린 소설",
                        ""),

                new Book("만년", "다자이 오사무", BookStatus.READING, 5,
                        "다자이 오사무의 초기 작품집",
                        "문장이 아름답고 우울하다."),

                new Book("안나 카레니나 1", "레프 톨스토이", BookStatus.WISH, 5,
                        "러시아 문학을 대표하는 장편소설",
                        ""),

                new Book("크눌프", "헤르만 헤세", BookStatus.DONE, 5,
                        "자유로운 방랑자의 삶을 그린 소설",
                        "헤세 작품 중에서도 특히 좋았다."),

                new Book("춘향전", "미상", BookStatus.WISH, 4,
                        "한국 고전문학의 대표 작품",
                        ""),

                new Book("말테의 수기", "라이너 마리아 릴케", BookStatus.READING, 4,
                        "릴케의 철학적 성찰이 담긴 소설",
                        "천천히 곱씹으며 읽고 있다."),

                new Book("젊은 베르테르의 슬픔", "요한 볼프강 폰 괴테", BookStatus.DONE, 5,
                        "낭만주의 문학의 대표작",
                        "왜 고전인지 이해할 수 있었다."),

                new Book("변신·시골의사", "프란츠 카프카", BookStatus.WISH, 5,
                        "카프카의 대표 중·단편 모음집",
                        "")


        );
    }

}
