import React, {useState} from 'react';
import {useOutletContext} from 'react-router-dom';

function Home() {
    const [count, setCount] = useState(0);
    const {isLoggedIn} = useOutletContext();

    return (
        <div className="row gtr-200">
            <div className="col-12">

                {/* Highlight */}
                <section className="box highlight">
                    <ul className="special">
                        <li><a href="#" className="icon solid fa-search"><span className="label">Magnifier</span></a>
                        </li>
                        <li><a href="#" className="icon solid fa-tablet-alt"><span className="label">Tablet</span></a>
                        </li>
                        <li><a href="#" className="icon solid fa-flask"><span className="label">Flask</span></a></li>
                        <li><a href="#" className="icon solid fa-cog"><span className="label">Cog?</span></a></li>
                    </ul>
                    <header>
                        <h2>A random assortment of icons in circles</h2>
                        <p>And some text that attempts to explain their significance</p>
                    </header>
                    <p>
                        Phasellus quam turpis, feugiat sit amet ornare in, hendrerit in lectus. Praesent semper mod quis
                        eget mi. Etiam eu<br/>
                        ante risus. Aliquam erat volutpat. Aliquam luctus et mattis lectus amet pulvinar. Nam nec turpis
                        consequat.
                    </p>
                </section>

            </div>
            <div className="col-12">

                {/* Features */}
                <section className="box features">
                    <h2 className="major"><span>A Major Heading</span></h2>
                    <div>
                        <div className="row">
                            <div className="col-3 col-6-medium col-12-small">

                                {/* Feature */}
                                <section className="box feature">
                                    <a href="#" className="image featured"><img src="images/pic01.jpg" alt=""/></a>
                                    <h3><a href="#">A Subheading</a></h3>
                                    <p>
                                        Phasellus quam turpis, feugiat sit amet ornare in, a hendrerit in
                                        lectus dolore. Praesent semper mod quis eget sed etiam eu ante risus.
                                    </p>
                                </section>

                            </div>
                            <div className="col-3 col-6-medium col-12-small">

                                {/* Feature */}
                                <section className="box feature">
                                    <a href="#" className="image featured"><img src="images/pic02.jpg" alt=""/></a>
                                    <h3><a href="#">Another Subheading</a></h3>
                                    <p>
                                        Phasellus quam turpis, feugiat sit amet ornare in, a hendrerit in
                                        lectus dolore. Praesent semper mod quis eget sed etiam eu ante risus.
                                    </p>
                                </section>

                            </div>
                            <div className="col-3 col-6-medium col-12-small">

                                {/* Feature */}
                                <section className="box feature">
                                    <a href="#" className="image featured"><img src="images/pic03.jpg" alt=""/></a>
                                    <h3><a href="#">And Another</a></h3>
                                    <p>
                                        Phasellus quam turpis, feugiat sit amet ornare in, a hendrerit in
                                        lectus dolore. Praesent semper mod quis eget sed etiam eu ante risus.
                                    </p>
                                </section>

                            </div>
                            <div className="col-3 col-6-medium col-12-small">

                                {/* Feature */}
                                <section className="box feature">
                                    <a href="#" className="image featured"><img src="images/pic04.jpg" alt=""/></a>
                                    <h3><a href="#">And One More</a></h3>
                                    <p>
                                        Phasellus quam turpis, feugiat sit amet ornare in, a hendrerit in
                                        lectus dolore. Praesent semper mod quis eget sed etiam eu ante risus.
                                    </p>
                                </section>

                            </div>
                            <div className="col-12">
                                <ul className="actions">
                                    <li><a href="#" className="button large">Do Something</a></li>
                                    <li><a href="#" className="button alt large">Think About It</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </section>

            </div>
            <div className="col-12">

                {/* Blog */}
                <section className="box blog">
                    <h2 className="major"><span>Another Major Heading</span></h2>
                    <div>
                        <div className="row">
                            <div className="col-9 col-12-medium">
                                <div className="content">

                                    {/* Featured Post */}
                                    <article className="box post">
                                        <header>
                                            <h3><a href="#">Here's a really big heading</a></h3>
                                            <p>With a smaller subtitle that attempts to elaborate</p>
                                            <ul className="meta">
                                                <li className="icon fa-clock">15 minutes ago</li>
                                                <li className="icon fa-comments"><a href="#">8</a></li>
                                            </ul>
                                        </header>
                                        <a href="#" className="image featured"><img src="images/pic05.jpg" alt=""/></a>
                                        <p>
                                            Phasellus quam turpis, feugiat sit amet ornare in, a hendrerit in lectus.
                                            Praesent
                                            semper mod quis eget mi. Etiam sed ante risus aliquam erat et volutpat.
                                            Praesent a
                                            dapibus velit. Curabitur sed nisi nunc, accumsan vestibulum lectus. Lorem
                                            ipsum
                                            dolor sit non aliquet sed, tempor et dolor. Praesent a dapibus velit.
                                            Curabitur
                                            accumsan.
                                        </p>
                                        <a href="#" className="button">Continue Reading</a>
                                    </article>

                                </div>
                            </div>
                            <div className="col-3 col-12-medium">
                                <div className="sidebar">

                                    {/* Archives */}
                                    <ul className="divided">
                                        <li>
                                            <article className="box post-summary">
                                                <h3><a href="#">A Subheading</a></h3>
                                                <ul className="meta">
                                                    <li className="icon fa-clock">6 hours ago</li>
                                                    <li className="icon fa-comments"><a href="#">34</a></li>
                                                </ul>
                                            </article>
                                        </li>
                                        <li>
                                            <article className="box post-summary">
                                                <h3><a href="#">Another Subheading</a></h3>
                                                <ul className="meta">
                                                    <li className="icon fa-clock">9 hours ago</li>
                                                    <li className="icon fa-comments"><a href="#">27</a></li>
                                                </ul>
                                            </article>
                                        </li>
                                        <li>
                                            <article className="box post-summary">
                                                <h3><a href="#">And Another</a></h3>
                                                <ul className="meta">
                                                    <li className="icon fa-clock">Yesterday</li>
                                                    <li className="icon fa-comments"><a href="#">184</a></li>
                                                </ul>
                                            </article>
                                        </li>
                                        <li>
                                            <article className="box post-summary">
                                                <h3><a href="#">And Another</a></h3>
                                                <ul className="meta">
                                                    <li className="icon fa-clock">2 days ago</li>
                                                    <li className="icon fa-comments"><a href="#">286</a></li>
                                                </ul>
                                            </article>
                                        </li>
                                        <li>
                                            <article className="box post-summary">
                                                <h3><a href="#">And One More</a></h3>
                                                <ul className="meta">
                                                    <li className="icon fa-clock">3 days ago</li>
                                                    <li className="icon fa-comments"><a href="#">8,086</a></li>
                                                </ul>
                                            </article>
                                        </li>
                                    </ul>
                                    <a href="#" className="button alt">Archives</a>

                                </div>
                            </div>
                        </div>
                    </div>
                </section>

            </div>
        </div>
    );
}

export default Home;