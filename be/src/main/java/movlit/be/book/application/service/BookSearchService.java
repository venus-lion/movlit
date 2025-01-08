package movlit.be.book.application.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.presentation.dto.BooksSearchResponseDto;
import movlit.be.bookES.BookES;
import movlit.be.bookES.BookESConvertor;
import movlit.be.bookES.BookESDomain;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookSearchService {
    private final ElasticsearchOperations elasticsearchOperations;
    public BooksSearchResponseDto getSearchBook(String inputStr, int page, int pageSize){
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        System.out.println("검색 시작 :::: " + inputStr);

        Query boolQuery = Query.of(q -> q
                .bool(bq -> bq.
                        should(
                            // 1. title
                            Query.of(q1 -> q1.match(m -> m.field("title").query(inputStr).boost(1.8F))),
                            Query.of(q1 -> q1.match(m -> m.field("title.ngram").query(inputStr).boost(1.8F))),
                            Query.of(fq -> fq.fuzzy(q1 -> q1.field("title.standard").value(inputStr)
                                    .fuzziness("AUTO").boost(1.4F))),
                            // 2. categoryName
                            Query.of(tq -> tq.term(q2 -> q2.field("categoryName").value(inputStr).boost(2.0F))),
                            Query.of(fq2 -> fq2.fuzzy(q3 -> q3.field("categoryName").value(inputStr)
                                    .fuzziness("AUTO").boost(2.0F))),
                            // 3. crew
                            Query.of(mq -> mq.match(q4 -> q4.field("crew").query(inputStr).boost(1.8F))),
                            Query.of(mq -> mq.match(q5 -> q5.field("crew.ngram").query(inputStr).boost(1.8F)))
                        )));

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(pageable)
                .withSort(Sort.by(
                        Order.desc("_score")
                ))
                .build();

        SearchHits<BookES> searchHits = elasticsearchOperations.search(nativeQuery, BookES.class);

        List<BookES> bookESList = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        for (BookES bookES : bookESList){
            System.out.println("그때마다의 bookES의 categoryName :: " + bookES.getCategoryName());
        }

        // Total Page 구하기
        int pageSize2 = pageable.getPageSize();
        long totalHits = searchHits.getTotalHits();
        long totalPages = totalHits / pageSize2;

        if (totalHits % pageSize2 != 0){ // 데이터가 완전히 나눠지지 않아, 한 페이지 추가
            totalPages++;
        }

        List<BookESDomain> bookESDomainList = bookESList.stream()
                .map(bookES -> BookESConvertor.documentToDomain(bookES))
                .collect(Collectors.toList());

        return BooksSearchResponseDto.builder()
                .bookESDomainList(bookESDomainList)
                .totalPages(totalPages)
                .build();
    }
}
