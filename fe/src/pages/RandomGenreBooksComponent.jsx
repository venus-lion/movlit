import React, {useState} from "react";
import useBookList from "../hooks/useBookList.jsx";
import BookGenreCarousel from "./BookGenreCarousel.jsx";

function RandomGenreBooksComponent() {
    const {books, loading, error} = useBookList({
        endpoint: '/books/genres/random',
        params: {limit: 30},
    });

    const [startIndex, setStartIndex] = useState(0); // 화면에 보이는 도서 시작 인덱스

    const handleNext = () => {
        const newIndex = startIndex + 5;
        if (newIndex < books.length) {
            setStartIndex(newIndex);
        }
    };

    const handlePrev = () => {
        const newIndex = startIndex - 5;
        if (newIndex >= 0) {
            setStartIndex(newIndex);
        }
    };

    if (loading) return <p>Loading books...</p>;
    if (error) return (
        <div>
            <p>Error loading popular books.</p>
        </div>
    );

    // 유니크한 장르 세트 생성
    const uniqueGenres = new Set();

    console.log('받아온 books :: ' + books);

    if (books) {
        books.forEach(book => {
            if (book.genres && Array.isArray(book.genres)) {
                book.genres.forEach(genre => uniqueGenres.add(genre.genreName));
            }
        });
    }

    const uniqueGenreList = Array.from(uniqueGenres);

    console.log('유니크한 장르 세트 :: ' + uniqueGenreList);
    return (
        <BookGenreCarousel
            title={`마니아를 위해: ${uniqueGenreList.join(', ')}`}
            books={books}
            startIndex={startIndex}
            handleNext={handleNext}
            handlePrev={handlePrev}
        ></BookGenreCarousel>
    );
}

export default RandomGenreBooksComponent;