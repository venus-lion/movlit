package movlit.be.book.presentation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.book.application.converter.service.BookCommentReadService;
import movlit.be.book.application.converter.service.BookCommentWriteService;
import movlit.be.book.application.converter.service.BookDetailReadService;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookDetailController {
    private final BookDetailReadService bookDetailReadService;
    private final BookCommentReadService bookCommentReadService;
    private final BookCommentWriteService bookCommentWriteService;


    @GetMapping("{bookId}/detail")
    @ResponseBody
    public Book findBook(@PathVariable BookId bookId){
        Book book = bookDetailReadService.findByBookId(bookId);
        return book;
    }


//    @GetMapping("{bookId}/comments")
//    @ResponseBody
//    public List<BookComment> bookCommentReadService(@PathVariable BookId bookId, @RequestParam(name = "p", defaultValue = "1") int page){
//        Page<BookComment> pagedResult = bookCommentReadService.getPagedBookComments(bookId, page);
//        int totalPages = pagedResult.getTotalPages();
//        int startPage = (int) Math.ceil((page - 0.5) / bookCommentReadService.PAGE_SIZE - 1) * bookCommentReadService.PAGE_SIZE + 1;
//        int endPage = Math.min(startPage + bookCommentReadService.PAGE_SIZE - 1, totalPages);
//        List<Integer> pageList = new ArrayList<>();
//        for (int i = startPage; i <= endPage; i++)
//            pageList.add(i);
//
//       return pagedResult.getContent();
//
//    }

    @PostMapping("{bookId}/detail")
    public String registerComment(@RequestBody BookComment bookComment) {
       BookComment receivedBookComment = bookComment;

        bookCommentWriteService.registerBookComment(receivedBookComment);
        return "성공";
    }

}
