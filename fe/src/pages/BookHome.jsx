import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './BookHome.css';
import {Link} from 'react-router-dom'; //  도서 디테일 페이지로 이동하기 위한 링크

import BestsellerBooksComponent from "./BestsellerBooksComponent.jsx";
import PopularBooksComponent from "./PopularBooksComponent.jsx";
import NewBooksComponent from "./NewBooksComponent.jsx";
function BookHome() {




    return (
        <div className="book-home">
            <BestsellerBooksComponent />
            <PopularBooksComponent />
            <NewBooksComponent />
        </div>

    );
}

export default BookHome;