optimizelyCode = function () {
    var DATA = {
        "public_suffixes": {"consumerreports.org": ["ec.consumerreports.org", "web.consumerreports.org", "www.consumerreports.org"]},
        "log_host": "log3.optimizely.com",
        "api_host": "api.optimizely.com",
        "variations": {
            "173719680": {"name": "Original"},
            "198364928": {"name": "Original"},
            "166494211": {
                "code": "/* _optimizely_redirect=http://web.consumerreports.org/test/SEM/version8.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'http://web.consumerreports.org/test/SEM/version8.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Variation #1"
            },
            "173746820": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n    <div id=\\\"header\\\">\\n        Experts You Can Trust        \\n    </div>\\n    <img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/direct-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\"/>\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports, the largest nonprofit educational and consumer product testing center in the world, tests over 5,000 products a year.\\n        </p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>We've been helping consumers get the best value for their dollar for over 75 years.</p></li>\\n                <li><p>We have a 100 product testing <strong><em>experts</em></strong> in seven major technical departments\u2014appliances, auto, baby &amp; kids, electronics, home &amp; garden, health, &amp; money.</p></li>\\n                <li><p>We have 50 state of the art labs at our headquarters in Yonkers, New York.</p></li>\\n                <li><p>Our 327 acre auto test track is staffed by more then 20 <strong><em>expert</em></strong> engineers &amp; technicians.</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/direct-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");",
                "name": "Variation #1"
            },
            "173731589": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:26px;padding-top:10px;\\\">Looking for Reliable Babies &amp; Kids Products?  We got you covered...</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/babies-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">When you are looking for babies &amp; kids products you'll want to know they have all the features you are looking for, but you will also want to know how reliable it is. We can help!</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Product performance is tested in our labs, but get reliability data from consumers like you who respond to our yearly reliability survey.</p></li>\\n                <li><p>Performance and reliability data are combined to help you find the products that are more likely to work well and be trouble free.</p></li>\\n                <li><p>Price and reliability are not necessarily related.  Don't spend more on babies &amp; kids products then you need to.</p></li>\\n        \\n               </ul>\\n          \\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"hhttps://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/babies-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/babies-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:26px;padding-top:10px;\\\">Looking for Safe & Reliable Babies &amp; Kids Products?  We got you covered...</div>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size: 26px; padding-top: 10px;\\\"><span style=\\\"font-size: x-large;\\\">Looking for Reliable Babies &amp; Kids Products?  We got you covered...</span></div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports can tell you not only what products test the best, but which ones last the longest.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Price and reliability are not necessarily related.  Don't spend more on appliances than you need to.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Over one million consumers give feedback allowing us to rate brands for quality of service and for product reliability.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Spend wisely on products that will last.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p><strong>Spend wisely on products that will last.</strong>\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Price and reliability are not necessarily related.  Don't spend more on babies and kids products than you need to.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>More than one million consumers give feedback, allowing us to rate brands for quality of service and for product reliability.\\n</p>\");",
                "name": "Variation #2"
            },
            "166776070": {
                "code": "/* _optimizely_redirect=http://web.consumerreports.org/tvs/v3/index.html */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'http://web.consumerreports.org/tvs/v3/index.html';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Variation #1"
            },
            "173721736": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:26px;padding-top:10px;\\\">\\n        Looking for Reliable New and Used Cars? We got you covered...     \\n    </div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/autos-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">When you are looking for a new or used car you'll want to know it has the features you are looking for, but you will also want to know how reliable it is. We can help!</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Get reliability information based on feedback from 1.3 million car drivers</p></li>\\n                <li><p>Most and least reliable vehicles from cars to trucks to SUVs and minivans</p></li>\\n                <li><p>Find out who makes the best cars</p></li>\\n                <li><p>Find out what redesigned models are recommended</p></li>\\n               </ul>\\n          \\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/autos-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:26px;padding-top:10px;\\\">\\n        Looking for a Car that will Last? We got you covered...     \\n    </div>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:26px;padding-top:10px;\\\">\\n        Looking for a car that will last? We got you covered...     \\n    </div>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:26px;padding-top:10px;\\\">\\n        Looking for a car that will be trouble free? We got you covered...     \\n    </div>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Ratings for new and used cars based on actual performance and reliability data</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Ratings for new and used cars based on actual performance and reliability data</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Get reliability information based on feedback from 1.3 million car drivers</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Ratings for new and used cars based on more than 50 individual tests performed by more than 20 expert engineers &amp; technicians</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Ratings for new and used cars based on more than 50 tests performed by more than 20 expert engineers &amp; technicians</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Predicted reliability on new cars based on the actual experiences of car owners.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Specific used car reliability verdicts covering 17 trouble spots of the vehicle based on feedback from 1.3 million car drivers</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Specific used car reliability verdicts covering 17 areas ranging from the engine, transmission and brakes to the electrical system</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">When you are looking for a new or used car you'll want to know how reliable it is. We can help!</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">With Consumer Reports test ans survey results, you'll be prepared to purchase a car that doesn't just run well, but lasts long and retains value.</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">With Consumer Reports test and survey results, you'll be prepared to purchase a car that doesn't just run well, but lasts long and retains value.</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/cars/car_testing.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:26px;padding-top:10px;\\\">\\n        Looking for a car that will be trouble free? We have you covered...     \\n    </div>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Specific used car reliability verdicts covering 17 areas ranging from the engine, transmission, and brakes to the electrical system</p>\");",
                "name": "Variation #2"
            },
            "173768329": {"name": "Original"},
            "173770122": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">We Have the Latest Electronics Product Ratings &amp; Reviews</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/electronics-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports\u00a0currently tests about 1,000 consumer electronic products every year, in two dozen or so categories. We cover everything from big-screen LCD and plasma TVs to cell phones, computers, digital cameras, and newer categories such as e-readers, netbooks, and 3D-capable TVs. No one conducts hands-on tests of as many products as we do.</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>We decide which electronic categories to test based on current trends and on what our subscribers are interested in buying.</p></li>\\n                <li><p>Our experienced product specialists consider market data to help them select specific models, with a focus on representative brands, price ranges, and important features.</p></li>\\n                <li><p>For the key categories, we produce new Ratings almost every month, and we update our Ratings of other types of products several times a year.</p></li>\\n                <li><p>We test innovative, high-interest products as soon as they become available</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/electronics-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">Expert Electronics & Computer Ratings\\n</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about 1,000 consumer electronics products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>We purchase all the products we test at retail, just like you.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state of the art labs at our headquarters in New York.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state of the art labs at our headquarters in New York.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>We test innovative, high-interest products as soon as they become available\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>We test innovative, high-interest products as soon as they become available.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state-of-the-art labs at our headquarters in New York.</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports tests about 1,000 consumer electronics products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");",
                "name": "Variation #3"
            },
            "125773506": {"name": "Original Page"},
            "196253968": {
                "code": "/* _optimizely_redirect= https://ec.consumerreports.org/ec/myaccount/cancel_subscription.htm?crSave1=CROsave */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = ' https://ec.consumerreports.org/ec/myaccount/cancel_subscription.htm';\nvar redirectSecond = 'crSave1=CROsave';\nif (window.location.search.indexOf('crSave1=CROsave') < 0) {  \nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);\n}",
                "name": "Variation #1"
            },
            "166789017": {"name": "Original"},
            "173750425": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n    <div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">We take the guesswork out of buying the BEST appliances</div>\\n    <img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/appliances-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\"/>\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Our experts test over 1,500 appliances and laundry and cleaning products. We cover everything from refrigerators to juicers, to air purifiers, ranges, dishwashers, dish detergents, vacuum cleaners, and more.</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Get expert buying advice, Ratings, reviews and brand reliability for the appliance products you are looking for</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/appliances-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">Your Appliance Buying Guide</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Everything you need to know to get the most from today's most popular appliances.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Save money with expert buying advice, Ratings and reviews.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our experts test about 1,000 consumer electronics products every year.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Find out how to get the best deals, as well as the most value\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our experts test about 1,000 consumer appliances every year.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our experts test over 1,500 consumer appliances every year.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Find out how to get the best deals, as well as the most value.</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/appliances/appliances_products.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Save money with expert buying advice, Ratings, and reviews.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Find out how to get the best deals and the most value.</p>\");",
                "name": "Variation #1"
            },
            "169443229": {
                "code": "/* _optimizely_redirect=https://ec.consumerreports.org/ec/cro/csd2ten/order.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'https://ec.consumerreports.org/ec/cro/csd2ten/order.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Billing-$10"
            },
            "173770142": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n    <div id=\\\"header\\\" style=\\\"font-size:24px;padding-top:15px;\\\">We take the guesswork out of buying the BEST babies &amp; kids products</div>\\n    <img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/babies-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\"/>\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Our experts test over \\\"\\\" baby &amp; kids products. We cover everything from car seats to monitors to breast pumps, strollers, high chairs, play yards and more.</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Get expert buying advice, Ratings, reviews and brand reliability for the babies &amp; kids products you are looking for.</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"hhttps://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/babies-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:24px;padding-top:15px;\\\">Your Babies & Kids Product Buying Guide</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Everything you need to know to get the safest Babies & Kids Products.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Save money with expert buying advice, Ratings and reviews.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our experts test about 1,000 Babies & Kids products every year.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Find out how to get the best deals, as well as the most value.\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our experts test over 250 Babies &amp; Kids products every year.\\n</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/babies%20and%20kids/babies_kids_testing.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Save money with expert buying advice, Ratings, and reviews.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our experts test more than 250 Babies &amp; Kids products every year.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Find out how to get the best deals and the most value.\\n</p>\");",
                "name": "Variation #1"
            },
            "175269381": {"name": "Original"},
            "166732320": {
                "code": "/* _optimizely_redirect=http://web.consumerreports.org/dishwashers/v2/index.html */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'http://web.consumerreports.org/dishwashers/v2/index.html';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Variation #1"
            },
            "173756322": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n    <div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">We take the guesswork out of buying the BEST electronics</div>\\n    <img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/electronics-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\"/>\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Our experts test about 1,000 consumer electronics products every year, in two dozen or so categories.  We cover everything from big-screen LCD and plasma TVs to cell phones, computers, digital cameras, and newer categories such as e-readers, netbooks, and 3D capable TVs.</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Get expert buying advice, Ratings, reviews and brand reliability for the electronics products you are looking for</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/electronics-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">Your Electronics Buying Guide</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Everything you need to know to get the most from today's hottest electronics products.\\n.</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Everything you need to know to get the most from today's hottest electronics products.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Save money with expert buying advice, Ratings and reviews.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our experts test about 1,000 consumer electronics products every year.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Find out how to get the best deals, as well as the most value.\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p></p>\");\n$(\".red-arrows > li:eq(3)\").replaceWith(\"<li></li>\");\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Save money with expert buying advice, Ratings, and reviews.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Find out how to get the best deals and the most value.\\n</p>\");",
                "name": "Variation #1"
            },
            "166411557": {"name": "Original"},
            "173768358": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:15px;\\\">COPY COPY COPY COPY COPY COPY COPY COPY COPY </div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/money-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/money-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:15px;\\\">Get Expert Banking & Credit Information </div>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Make smart banking & credit decisions with Consumer Reports expert advice.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Find out how to choose the best credit card with the most attractive offers and how to avoid teaser rates.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Get the information you need on the hidden fees associated with prepaid credit cards\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p>Learn how to get the most out of your rewards cards to increase your rewards.\\n</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Make smart banking &amp; credit decisions with Consumer Reports expert advice.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Find out how to choose the best credit card with the most attractive offers and how to avoid teaser rates.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Get the information you need on the hidden fees associated with prepaid credit cards.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Learn how to get the most out of your rewards cards to increase your rewards.\\n</p>\");\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:15px;\\\">How to Save Protect and Grow Your Money </div>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:15px;\\\">Enjoy a Richer Retirement</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports advises you on the most effective strategies for building a bigger nest egg that can weather market storms.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>How to make significant cuts in your expenses.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Ways to lower your taxes.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Do's and Don'ts of long-term care insurance.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Avoid tax-time headaches.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Make sense of money advisors.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Keys to a great retirement.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Make sense of money advisers.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Money mistakes to avoid.\\n</p>\");\n$(\".red-arrows > li:eq(2)\").append(\"<div id=\\\"optimizely_434575923\\\">...</div>\");\n$(\"#optimizely_434575923\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\"#content-right\").append(\"<div id=\\\"optimizely_481801137\\\">...</div>\");\n$(\".red-arrows\").replaceWith(\"<ul class=\\\"red-arrows\\\">\\n                <li><p>Avoid tax-time headaches.\\n</p></li>\\n                <li><p>Make sense of money advisers.\\n</p></li>\\n                <li><p>Money mistakes to avoid.\\n<li><p>And more!</p></li>\\n</p><div id=\\\"optimizely_434575923\\\" style=\\\"display: none;\\\">...</div></li>\\n\\n               </ul>\");",
                "name": "Variation #3"
            },
            "166800039": {"name": "Original"},
            "173746856": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n    <div id=\\\"header\\\" style=\\\"font-size:24px;padding-top:15px;\\\">We take the guesswork out of buying the BEST Home &amp; Garden Products</div>\\n    <img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/homegarden-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\"/>\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Our experts test over \\\"\\\" home &amp; garden products. We cover everything from treadmills to mattresses to ellipticals, electric shavers, flat irons, snow blowers and more.</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Get expert buying advice, Ratings, reviews and brand reliability for the home &amp; garden products you are looking for.</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homegarden-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:24px;padding-top:15px;\\\">Your Home & Garden Product Buying Guide</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Everything you need to know to get the best Home & Garden products.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Save money with expert buying advice, Ratings and reviews.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our experts test about 1,000 consumer Home & Garden products every year.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Find out how to get the best deals, as well as the most value.\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Save money with expert buying advice, Ratings, and reviews.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Find out how to get the best deals and the most value.\\n</p>\");",
                "name": "Variation #1"
            },
            "169175935": {
                "code": "/* _optimizely_redirect=https://ec.consumerreports.org/ec/cro/csd1ten/order.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'https://ec.consumerreports.org/ec/cro/csd1ten/order.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Add-to-Cart-$10"
            },
            "116686635": {
                "code": "$(\"body\").append(\"<div id=\\\"facebox\\\">  <div>    <h2>Lightbox Demo</h2>    <p>      This dialog is opened programmatically when the page      loads. There is no need for a trigger element.    </p>       <p style=\\\"color:#666\\\">      To close, click the Close button or hit the ESC key.    </p>    <!-- yes/no buttons -->    <p>      <button class=\\\"close\\\"> Close </button>    </p>  </div></div>\");\n\n$(function(){var fileRef1 = document.createElement('script'); fileRef1.setAttribute(\"type\",\"text/javascript\"); fileRef1.setAttribute(\"src\",\"http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js\"); document.body.appendChild(fileRef1);});\n\n$(function(){var fileRef2 = document.createElement('script'); fileRef2.setAttribute(\"type\",\"text/javascript\"); fileRef2.setAttribute(\"src\", \"http://cdn.jquerytools.org/1.2.6/all/jquery.tools.min.js\"); document.body.appendChild(fileRef2);});\n \nvar testOverlay = setInterval(function(){\n  //console.log('Run Interval');\n  if(typeof window.jQuery(\"#facebox\").overlay == 'function'){\n  window.jQuery(\"#facebox\").overlay({\n\n  // custom top position\n  top: 260,\n\n  // some mask tweaks suitable for facebox-looking dialogs\n  mask: {\n\n  // you might also consider a \"transparent\" color for the mask\n  color: '#fff',\n\n  // load mask a little faster\n  loadSpeed: 200,\n\n  // very transparent\n  opacity: 0.5\n  },\n\n  // disable this for modal dialog-type of overlays\n  closeOnClick: false,\n\n  // load it immediately after the construction\n  load: true\n  });\n\n  clearInterval(testOverlay);\n    }}, 50);",
                "name": "Variation #1"
            },
            "169181961": {
                "code": "/* _optimizely_redirect=https://ec.consumerreports.org/ec/cro/csd3ten/order.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'https://ec.consumerreports.org/ec/cro/csd3ten/order.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Conf-$10"
            },
            "173767154": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n        A Fair, Just &amp; Safe Marketplace For All Consumers       \\n    </div>\\n    <img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/direct-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports is an expert, independent, nonprofit organization whose mission is to work for a fair, just, and safe marketplace for all consumers.\\n        </p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p><strong>Did you know?</strong> Consumer Reports accepts no outside advertising and no free samples.</p></li>\\n                <li><p><strong>Did you know?</strong> Consumer Reports rigorously tests over 5,000 products a year.</p></li>\\n                <li><p><strong>Did you know?</strong> We purchase all the products we test at retail, just like you.</p></li>\\n                <li><p><strong>Did you know?</strong> We've been testing products since 1936.</p></li>\\n               </ul>\\n          <div style=\\\"margin-left:30px;margin-top:10px;font-size:18px;font-weight:bold;\\\">NOW YOU KNOW!  SUBSCRIBE TODAY</div>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/direct-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Consumer Reports accepts no outside advertising and no free samples.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Consumer Reports rigorously tests over 5,000 products a year.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>We purchase all the products we test at retail, just like you.</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p>We've been testing products since 1936.</p>\");\n$(\"#copy > div\").replaceWith(\"<div style=\\\"margin-left:30px;margin-top:10px;font-size:18px;font-weight:bold;\\\"></div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homepage-test-var1a.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n        A Fair Marketplace For All Consumers       \\n    </div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").css({\"font-size\":\"18\"});",
                "name": "Variation #2"
            },
            "166669958": {"name": "Original"},
            "173733552": {"name": "Original"},
            "173734600": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n       Largest Independent Testing Organization In The World      \\n    </div>\\n    <img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/direct-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">The dedicated professionals at Consumer Reports work tirelessly on your behalf every day. We are the largest and most trusted consumer product safety organization in the world, and our efforts have never been more important. Your support makes all our work possible.\\n        </p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p><strong>Did you know?</strong> Consumer Reports accepts no outside advertising or corporate contributions.</p></li>\\n                <li><p><strong>Did you know?</strong> Consumer Reports accepts no free samples.</p></li>\\n                <li><p><strong>Did you know?</strong> Did you know?  Mystery shoppers purchase all the products we test at retail, just like you.</p></li>\\n                <li><p><strong>Your contributions enable us to maintain that impeccable integrity.</strong> </p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/direct-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size: 18px; margin-top: 10px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: rgb(218, 218, 218); padding: 5px 0px 10px;\\\">Consumer Reports work tirelessly on your behalf every day. We are the most trusted consumer product safety organization in the world, and our efforts have never been more important. Your support makes all our work possible.\\n        </p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Consumer Reports accepts no outside advertising or corporate contributions.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Consumer Reports accepts no free samples.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Mystery shoppers purchase all the products we test at retail, just like you.</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homepage-test-var1a.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Secret shoppers purchase all the products we test at retail, just like you.</p>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n       Largest Independent Testing Organization \\n    </div>\");",
                "name": "Variation #3"
            },
            "76274047": {
                "code": "$(\"body\").append(\"<div id=\\\"facebox\\\">  <div>    <h2>Lightbox Demo</h2>    <p>      This dialog is opened programmatically when the page      loads. There is no need for a trigger element.    </p>       <p style=\\\"color:#666\\\">      To close, click the Close button or hit the ESC key.    </p>    <!-- yes/no buttons -->    <p>      <button class=\\\"close\\\"> Subscribe for the special price of $xx</button>    </p>  </div></div>\");\n\n$(function(){var fileRef1 = document.createElement('script'); fileRef1.setAttribute(\"type\",\"text/javascript\"); fileRef1.setAttribute(\"src\",\"http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js\"); document.body.appendChild(fileRef1);});\n\n$(function(){var fileRef2 = document.createElement('script'); fileRef2.setAttribute(\"type\",\"text/javascript\"); fileRef2.setAttribute(\"src\", \"http://cdn.jquerytools.org/1.2.6/all/jquery.tools.min.js\"); document.body.appendChild(fileRef2);});\n \nvar testOverlay = setInterval(function(){\n  //console.log('Run Interval');\n  if(typeof window.jQuery(\"#facebox\").overlay == 'function'){\n  window.jQuery(\"#facebox\").overlay({\n\n  // custom top position\n  top: 2,\n\n  // some mask tweaks suitable for facebox-looking dialogs\n  mask: {\n\n  // you might also consider a \"transparent\" color for the mask\n  color: '#fff',\n\n  // load mask a little faster\n  loadSpeed: 20,\n\n  // very transparent\n  opacity: 0.5\n  },\n\n  // disable this for modal dialog-type of overlays\n  closeOnClick: false,\n\n  // load it immediately after the construction\n  load: true\n  });\n\n  clearInterval(testOverlay);\n    }}, 50);",
                "name": "Variation #1"
            },
            "173766324": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n       We Have the Latest Car Ratings &amp; Reviews     \\n    </div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/autos-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">COPY COPYCOPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY  COPY COPY  COPY COPY   </p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/autos-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n       Expert Car Ratings \\n    </div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about 1,000 consumer electronics products every year.  We help you spend wisely by telling you which products perform the best. </p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about 80 cars every year.  We help you spend wisely by telling you which cars perform the best. </p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>et <strong><em>expert</em></strong> car reviews and Ratings, for new and used&nbsp;cars, with performance, pricing, and reliability data.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Get <strong><em>expert</em></strong> car reviews and Ratings, for new and used&nbsp;cars, with performance, pricing, and reliability data.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our car testing evaluation regimen consists of more than 50 individual tests.</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports throughly tests all cars, putting them through over 50 tests and driving each vehicle for thousands of miles. </p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports throughly tests all cars, driving each vehicle for thousands of miles while putting them through over 50 performance-related tests. </p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Individual Ratings for Safety, Performance, Comfort & Convenience and Fuel Economy.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Ratings for safety performance, comfort &amp; convenience and fuel economy.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Ratings for safety, performance, comfort &amp; convenience and fuel economy.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Ratings for safety, performance, comfort &amp; convenience and fuel economy based on our engineers test results.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Ratings for safety, performance, comfort &amp; convenience and fuel economy based on our expert test results.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Ratings for predicted new car reliability and owner satisfaction based on the survey results of 1.3 million subscribers.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Predicted depreciation and owner costs scores to help you compare how expensive vehicles will be over the long term. </p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Predicted depreciation and owner cost ratings to help you compare how expensive vehicles will be over the long term. </p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Predicted depreciation and owner cost Ratings to help you compare how expensive vehicles will be over the long term. </p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/cars/car_testing_2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports throughly tests all cars, driving each vehicle for thousands of miles while putting them through more than 50 performance-related tests. </p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Ratings for safety, performance, comfort &amp; convenience, and fuel economy based on our expert test results.</p>\");",
                "name": "Variation #3"
            },
            "173739445": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n    <div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n        We take the guesswork out of buying the BEST cars       \\n    </div>\\n    <img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/autos-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\"/>\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Our experts test \\\" \\\" number of cars, and then narrow the results down to recommended models \u2013 those that deserve special consideration. We highlight top-ranked cars that combine performance, features, reliability and value \u2013 all so that you can make a smart purchase that best suits your budget and lifestyle.\\n        </p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Our 327 acre auto test track is staffed by more then 20 <strong><em>expert</em></strong> engineers &amp; technicians.</p></li>\\n                <li><p>Get <strong><em>expert</em></strong> car reviews and Ratings, for new and used\u00a0cars, with performance, pricing, and reliability data.</p></li>\\n                <li><p>Our car testing evaluation regimen consists of more than 50 individual tests</p></li>\\n                <li><p>Find out how much that next car repair should cost.</p></li>\\n                <li><p>Our <strong><em>experts</em></strong> test over 400 tire and care products such as tires, car batteries and car wax.</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/autos-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Everything you need to know to get the most from today's most popular cars.\\n        </p>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size: 32px; padding-top: 5px;\\\">Your Car Buying Guide</div>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Our car testing evaluation regimen consists of more than 50 individual tests performed by more then 20 <strong><em>expert</em></strong> engineers &amp; technicians.</p></p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Get <strong><em>expert</em></strong> car reviews and Ratings, for new and used&nbsp;cars, with performance, pricing, and reliability data.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Our car testing evaluation regimen consists of more than 50 individual tests performed by more then 20 <strong><em>expert</em></strong> engineers &amp; technicians.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Ratings for new and used cars based on actual performance and reliability data.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Each car goes through more than 50 individual tests performed by more than 20 <strong><em>expert</em></strong> engineers &amp; technicians.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Each car goes through more than 50 individual tests performed by more than 20 expert engineers &amp; technicians.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Consumer Reports Build & Buy Program saves subscribers an average of $2,743 off MSRP.</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p>Ratings of over 400 tire and care products such as tires, car batteries and car wax.</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p>Ratings of over 400 after market products such as tires, car batteries and car wax.</p>\");\n$(\".red-arrows > li:eq(4) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(4)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/cars/car_products.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p>Ratings of over 400 after market products such as tires, car batteries, and car wax.</p>\");",
                "name": "Variation #1"
            },
            "173768374": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:30px;padding-top:10px;\\\">Know the TRUTH about vitamins and supplements</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/health-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">The experts at Consmer Reports provide unbiased natural health information, including vitamins and supplements information.</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Get up to date information on natural health from our experts</p></li>\\n                <li><p>Get Consumer Reports guide to 100+ Supplements</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/health-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Get the truth on natural health remedies\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:30px;padding-top:10px;\\\">Know the TRUTH about Vitamins and Supplements</div>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:30px;padding-top:10px;\\\">Consumer Reports Your Source for Healthy Living</div>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Diet & Exercise Equipment Ratings:  How you should loose weight and keep it off.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p><strong>Diet &amp; Exercise Equipment Ratings:</strong>  How you should loose weight and keep it off.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p><strong>Diet &amp; Exercise Equipment Ratings:</strong>  How you should lose weight and keep it off.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Home Medical Supply Ratings:</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p><strong>Home Medical Supply Ratings:</strong></p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>Home Medical Supply Ratings:Blood pressure monitors, blood glucose meters, heart rate monitors and more.</strong>\");\n$(\".red-arrows > li:eq(1) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>.</strong>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Home Medical Supply Ratings:Blood pressure monitors, blood glucose meters, heart rate monitors and more.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>COnditions & Treatments:<strong>.</strong></p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Conditions &amp; Treatments:Expert Information on common health conditions and how best to treat them.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p><strong>Conditions &amp; Treatments:&nbsp;</strong>Expert Information on common health conditions and how best to treat them.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p><strong>Home Medical Supply Ratings:</strong>Blood pressure monitors, blood glucose meters, heart rate monitors and more.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>Home Medical Supply Ratings: </strong>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Get solid, scientific research translated into simple, do-able, how-to advice that makes healthy living easy.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p><strong>Home Medical Supply Ratings: </strong>Blood pressure monitors, blood glucose meters, heart rate monitors and more...\\n</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/health/health_product.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Get solid, scientific research translated into simple, doable, how-to advice that makes healthy living easy.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p><strong>Home Medical Supply Ratings: </strong>Blood pressure monitors, blood glucose meters, heart rate monitors, and more...\\n</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/health-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");",
                "name": "Variation #3"
            },
            "166111625": {"name": "Original"},
            "169431353": {"name": "Original"},
            "166777183": {
                "code": "/* _optimizely_redirect=http://web.consumerreports.org/vacuums/v3/index.html */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'http://web.consumerreports.org/vacuums/v3/index.html';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Variation #1"
            },
            "173762206": {"name": "Original"},
            "173760575": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:26px;padding-top:10px;\\\">Consumer Reports is your TRUSTED source for health information</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/health-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Get fact-based health information and health product Ratings and reviews from the unbiased experts at Consumer Reports</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p><strong>Looking for a Doctor</strong> - Use our resources to help you find a doctor and maintain a good doctor-patient relationship</p></li>\\n                <li><p><strong>How safe is your hospital</strong> - Get detailed Ratings of over 4,000 hospitals to help you find the best hospital in your area.</p></li>\\n                <li><p><strong>Heart Bypass Surgery Ratings</strong> - Use our Ratings of over 300 heart bypass surgery practices to help you find the best surgical group in your area.</p></li>\\n                <li><p><strong>Get in shape</strong> - We rate exercise equipment from treadmills to ellipticals to infomercial equipment</p></li>        \\n               </ul>\\n          \\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/health-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size: 26px; padding-top: 10px;\\\"><span style=\\\"font-size: x-large;\\\">Consumer Reports is your TRUSTED source for health information</span></div>\");\n$(\".red-arrows > li:eq(0) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>Looking for Heath Insurance</strong>\");\n$(\".red-arrows > li:eq(0) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>Looking for Health Insurance</strong>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p><strong>Shopping for Health Insurance</strong> - Use our resources to help you find health insurance</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p><strong>Get in shape</strong> - We rate exercise equipment from treadmills to ellipticals to infomercial equipment.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p><strong>Shopping for Health Insurance</strong> - Use our resources to help you find health insurance.</p>\");\n$(\"#header > span\").replaceWith(\"<span style=\\\"font-size: x-large;\\\">Consumer Reports is your TRUSTED source for Health Information</span>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Get fact-based health information and health product Ratings from the unbiased experts at Consumer Reports</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Get fact-based health information and health product Ratings from the unbiased experts at Consumer Reports.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>Your Health Insurance Buying Guide:</strong>\");\n$(\".red-arrows > li:eq(1) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>Ratings of Over 4,000 Hospitals</strong>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Get fact-based health information and product Ratings from the unbiased experts at Consumer Reports.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>Conditoins & Treatments</strong>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p><strong>Conditoins &amp; Treatments</strong> </p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p><strong>Drugs & Natural Supplements\\n  </p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p><strong>Ratings of Over 4,000 Hospitals</strong> </p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p><strong>Your Health Insurance Buying Guide</strong> - </p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>Conditions &amp; Treatments</strong>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p><strong>Your Health Insurance Buying Guide</strong></p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0) > strong:eq(0)\").replaceWith(\"<span style=\\\"font-size: large;\\\"><strong>Your Health Insurance Buying Guide</strong></span>\");\n$(\".red-arrows > li:eq(1) > p:eq(0) > strong:eq(0)\").replaceWith(\"<span style=\\\"font-size: large;\\\"><strong>Ratings of Over 4,000 Hospitals</strong></span>\");\n$(\".red-arrows > li:eq(2) > p:eq(0) > strong:eq(0)\").replaceWith(\"<span style=\\\"font-size: large;\\\">Conditions &amp; Treatments</span>\");\n$(\"p > strong\").replaceWith(\"<span style=\\\"font-size: large;\\\"><strong>Drugs &amp; Natural Supplements\\n  </strong></span>\");\n$(\".red-arrows > li:eq(2) > p:eq(0) > span:eq(0)\").replaceWith(\"<strong><span style=\\\"font-size: large;\\\">Conditions &amp; Treatments</span></strong>\");\n$(\".red-arrows > li:eq(1) > p:eq(0) > span:eq(0) > strong:eq(0)\").replaceWith(\"<strong>Ratings to Find the Bests Hospitals</strong>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/health/health_product.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/health-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");",
                "name": "Variation #2"
            },
            "187493314": {"name": "Variation #4"},
            "173723715": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">We have the Latest Home &amp; Garden Product Ratings &amp; Reviews</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/homegarden-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homegarden-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">Expert Home & Garden Product Ratings\\n</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about 1,000 consumer electronics products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>We purchase all the products we test at retail, just like you.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state of the art labs at our headquarters in New York.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>We test innovative, high-interest products as soon as they become available.\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about Home & Garden products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about 1,000 Home &amp; Garden products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/home_garden/home_garden_testing.jpg\\n\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state-of-the-art labs at our headquarters in New York.\\n</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports tests about 1,000 Home &amp; Garden products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");",
                "name": "Variation #3"
            },
            "173757961": {"name": "Original"},
            "173758535": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:26px;padding-top:10px;\\\">Looking for Reliable Home &amp; Garden Products?  We got you covered...</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/homegarden-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">When you are looking for home &amp; garden products you'll want to know they have the features you are looking for, but you will also want to know how reliable it is.  We can help!</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Product performance is tested in our labs, but get reliability data from consumers like you who respond to our yearly reliability survey.</p></li>\\n                <li><p>Performance and reliability data are combined to help you find the products that are more likely to work well and be trouble free.</p></li>\\n                <li><p>Price and reliability are not necessarily related.  Don't spend more on appliances then you need to.</p></li>\\n        \\n               </ul>\\n          \\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homegarden-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size: 26px; padding-top: 10px;\\\"><span style=\\\"font-size: x-large;\\\">Looking for Reliable Home &amp; Garden Products?  We got you covered...</span></div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports can tell you not only what products test the best, but which ones last the longest.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Price and reliability are not necessarily related.  Don't spend more on appliances than you need to.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Over one million consumers give feedback allowing us to rate brands for quality of service and for product reliability.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Spend wisely on products that will last.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p><strong>Spend wisely on products that will last.</strong>\\n</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/home_garden/home_garden_products.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header > span\").replaceWith(\"<span style=\\\"font-size: x-large;\\\">Looking for Reliable Home &amp; Garden Products?  We have you covered...</span>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>More than one million consumers give feedback, allowing us to rate brands for quality of service and for product reliability.\\n</p>\");",
                "name": "Variation #2"
            },
            "175050824": {"name": "Variation #1"},
            "166704074": {"name": "Original"},
            "169284230": {"name": "Original Page"},
            "166698317": {
                "code": "/* _optimizely_redirect=http://web.consumerreports.org/washingmachines/v2/index.html */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'http://web.consumerreports.org/washingmachines/v2/index.html';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Variation #1"
            },
            "166562638": {"name": "Original"},
            "173775055": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n    <div id=\\\"header\\\" style=\\\"font-size:30px;padding-top:10px;\\\">Get Expert Money &amp; Personal Finance Information</div>\\n    <img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/money-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\"/>\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Get expert advice that helps you make smarter choices on all types of personal financial decisions.</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Know when to dump a loser stock</p></li>\\n                <li><p>Make sure you can retire comfortably</p></li>\\n                <li><p>Get more insurance coverage for less money</p></li>\\n                <li><p>And more!</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/money-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Unbiased information, expert advice and easy-to-use recommendations for real-life finances.</p>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:30px;padding-top:10px;\\\">Expert Money &amp; Personal Finance Information</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Unbiased information, expert advice and easy-to-use recommendations for real-life finances on topics like...</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Personal savings</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Investments</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Credit cards</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Personal savings & retirement</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Investments such as stocks, mutual funds and annuities</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Credit cards & debt consolidation</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Personal savings, insurance and retirement</p>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:30px;padding-top:10px;\\\">How to Save Protect & Grow Your Money</div>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:30px;padding-top:10px;\\\">How to Save, Protect &amp; Grow Your Money</div>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Investing in stocks, mutual funds and annuities</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Unbiased information, expert advice and easy-to-use recommendations for real-life finances on topics such as...</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Personal savings, insurance, and retirement</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Investing in stocks, mutual funds, and annuities</p>\");",
                "name": "Variation #1"
            },
            "173700946": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:30px;padding-top:10px;\\\">\\n        Looking for Reliable Electronics?  We got you covered...   \\n    </div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/electronics-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">When you are looking for a new electronics you'll want to know it has the features you are looking for, but you will also want to know how reliable it is.  We can help!</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Product performance is tested in our labs, but get reliability data from consumers like you who respond to our yearly reliability survey.</p></li>\\n                <li><p>Performance and reliability data are combined to help you find the products that are more likely to work well and be trouble free.</p></li>\\n                <li><p>Price and reliability are not necessarily related.  Don't spend more on electronics then you need to.</p></li>\\n        \\n               </ul>\\n          \\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/electronics-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports can tell you not only what products test the best, but which ones last the longest.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Price and reliability are not necessarily related.  Don't spend more on electronics than you need to.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Over one million consumers give feedback allowing us to rate brands for quality of service and for product reliability.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Spend wisely on products that will last\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p><strong>Spend wisely on products that will last</strong>\\n</p>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size: 30px; padding-top: 10px;\\\">\\n        <span style=\\\"font-size: x-large;\\\">Looking for Reliable Electronics?  We got you covered...</span>   \\n    </div>\");\n$(\"strong\").replaceWith(\"<strong>Spend wisely on products that will last.</strong>\");\n$(\"#header > span\").replaceWith(\"<span style=\\\"font-size: x-large;\\\">Looking for Reliable Electronics?  We have you covered...</span>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>More than million consumers give feedback, allowing us to rate brands for quality of service and for product reliability.\\n</p>\");",
                "name": "Variation #2"
            },
            "173700963": {"name": "Original"},
            "169246670": {
                "code": "/* _optimizely_redirect=https://ec.consumerreports.org/ec/cro/cs1/order.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'https://ec.consumerreports.org/ec/cro/cs1/order.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Variation #1"
            },
            "173775063": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:22px;padding-top:15px;\\\">Get Expert Prevention and Treatment Information on Various Health Conditions</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/health-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p><strong>Doctors &amp; Health Care</strong> - We'll help you decide what approach and option is best for your needs</p></li>\\n                <li><p><strong>The best treatments</strong> - Get tips on managing your condition along with information on what treatments work</p></li>\\n                <li><p><strong>Common Conditions</strong> - Get expert information on common conditions from ADHD to heart disease</p></li>\\n               </ul>\\n          <div style=\\\"margin-left:30px;margin-top:5px;font-size:18px;font-weight:bold;\\\">Your health is important - Trust Consumer Reports</div>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/health-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:22px;padding-top:15px;\\\">Get Expert Health Prevention and Treatment Information</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Get fact-based health prevention and treatment information from the unbiased experts at Consumer Reports.</p>\");\n$(\"#copy > div\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(1) > p:eq(0) > strong:eq(0)\").replaceWith(\"<strong>The Best Treatments</strong>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p><strong>Doctors &amp; Health Care</strong> - We'll help you decide what approach and option is best for your needs.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p><strong>The Best Treatments</strong> - Get tips on managing your condition along with information on what treatments work.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p><strong>Common Conditions</strong> - Get expert information on common conditions from ADHD to heart disease.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p><strong>The Best Treatments</strong> - Get tips on managing your condition along with information on what treatments work.</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/health/health_testing.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/health-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");",
                "name": "Variation #1"
            },
            "166632589": {
                "code": "/* _optimizely_redirect=http://web.consumerreports.org/refrigerators/v2/index.html */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'http://web.consumerreports.org/refrigerators/v2/index.html';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Variation #1"
            },
            "125797593": {
                "code": "/* _optimizely_redirect=https://ec.consumerreports.org/ec/cro/cs1/order.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'https://ec.consumerreports.org/ec/cro/cs1/order.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Variation #1"
            },
            "169425372": {
                "code": "/* _optimizely_redirect=https://ec.consumerreports.org/ec/cro/csd1five/order.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'https://ec.consumerreports.org/ec/cro/csd1five/order.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Add-to-Cart-$5"
            },
            "173768285": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n    <div id=\\\"header\\\">\\n        Experts You Can Trust        \\n    </div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/homepage-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\"/>\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports, the largest nonprofit educational and consumer product testing center in the world, tests over 5,000 products a year.\\n        </p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>We've been helping consumers get the best value for their dollar for over 75 years.</p></li>\\n                <li><p>We have a 100 product testing <strong><em>experts</em></strong> in seven major technical departments\u2014appliances, auto, baby &amp; kids, electronics, home &amp; garden, health, &amp; money.</p></li>\\n                <li><p>We have 50 state of the art labs at our headquarters in Yonkers, New York.</p></li>\\n                <li><p>Our 327 acre auto test track is staffed by more then 20 <strong><em>expert</em></strong> engineers &amp; technicians.</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/homepage-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homepage-test-var1.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homepage-test-var1a.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").css({\"font-size\":\"16\"});\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>We have 50 state of the art labs at our headquarters in New York.</p>\");",
                "name": "Variation #1"
            },
            "173735519": {"name": "Original"},
            "78394467": {"name": "Original Page"},
            "173778086": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:36px;padding-top:15x;\\\">Be a SMART Shopper</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/money-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Learn the best shopping tips and the best places to shop</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Get store Ratings on appliance, electronics, and computer stores.</p></li>\\n                <li><p>Retail store shopping vs. online shopping</p></li>\\n                <li><p>Be a smart outlet store shopper</p></li>\\n                <li><p>Get shopping website Ratings</p></li>\\n                <li><p>And more!</p></li>\\n        \\n               </ul>\\n          \\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/money-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:36px;padding-top:15x;\\\">You can be a SMART Shopper</div>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Get Ratings on appliance, electronics, and computer retail stores.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Get Ratings on appliance, electronics, and computer retail stores</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p>Shopping website Ratings based on Consumer Reports surveyed readers</p>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:36px;padding-top:15x;\\\">Keep More of Your Cash</div>\");",
                "name": "Variation #2"
            },
            "173705823": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n       Largest Independent Testing Organization In The World      \\n    </div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/homepage-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">The dedicated professionals at Consumer Reports work tirelessly on your behalf every day. We are the largest and most trusted consumer product safety organization in the world, and our efforts have never been more important. Your support makes all our work possible.\\n        </p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p><strong>Did you know?</strong> Consumer Reports accepts no outside advertising or corporate contributions.</p></li>\\n                <li><p><strong>Did you know?</strong> Consumer Reports accepts no free samples.</p></li>\\n                <li><p><strong>Did you know?</strong> Did you know?  Mystery shoppers purchase all the products we test at retail, just like you.</p></li>\\n                <li><p><strong>Your contributions enable us to maintain that impeccable integrity.</strong> </p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/homepage-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homepage-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n       Largest Independent Testing Organization\\n    </div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports work tirelessly on your behalf every day. We are the largest and most trusted consumer product safety organization in the world, and our efforts have never been more important. Your support makes all our work possible.\\n        </p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").css({\"font-size\":\"16px\"});\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").css({\"font-size\":\"16px\"});\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").css({\"font-size\":\"14\"});\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size: 16px; margin-top: 10px; border-bottom: 1px solid rgb(218, 218, 218); padding: 5px 0px 10px;\\\">Consumer Reports work tirelessly on your behalf every day. We are the most trusted consumer product safety organization in the world, and our efforts have never been more important. Your support makes all our work possible.\\n        </p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Mystery shoppers purchase all the products we test at retail, just like you.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Consumer Reports accepts no free samples.</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Consumer Reports accepts no outside advertising or corporate contributions.</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").css({\"font-size\":\"18px\"});\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homepage-test-var1a.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" border=\\\"0\\\" width=\\\"203\\\">\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Secret shoppers purchase all the products we test at retail, just like you.</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size: 18px; margin-top: 10px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: rgb(218, 218, 218); padding: 5px 0px 10px;\\\">Consumer Reports works tirelessly on your behalf every day. We are the most trusted consumer product safety organization in the world, and our efforts have never been more important. Your support makes all our work possible.\\n        </p>\");",
                "name": "Variation #3"
            },
            "83552873": {"name": "Original Page"},
            "173761257": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n        A Fair, Just &amp; Safe Marketplace For All Consumers       \\n    </div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/homepage-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports is an expert, independent, nonprofit organization whose mission is to work for a fair, just, and safe marketplace for all consumers.\\n        </p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p><strong>Did you know?</strong> Consumer Reports accepts no outside advertising and no free samples.</p></li>\\n                <li><p><strong>Did you know?</strong> Consumer Reports rigorously tests over 5,000 products a year.</p></li>\\n                <li><p><strong>Did you know?</strong> We purchase all the products we test at retail, just like you.</p></li>\\n                <li><p><strong>Did you know?</strong> We've been testing products since 1936.</p></li>\\n               </ul>\\n          <div style=\\\"margin-left:30px;margin-top:10px;font-size:18px;font-weight:bold;\\\">NOW YOU KNOW!  SUBSCRIBE TODAY</div>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://www.consumerreports.org/cro/resources/streaming/mvt/homepage-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homepage-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Consumer Reports accepts no outside advertising and no free samples.</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Consumer Reports rigorously tests over 5,000 products a year.</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>We purchase all the products we test at retail, just like you.</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").replaceWith(\"<p>We've been testing products since 1936.</p>\");\n$(\"#copy > div\").replaceWith(\"<div style=\\\"margin-left:30px;margin-top:10px;font-size:18px;font-weight:bold;\\\"></div>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:32px;padding-top:5px;\\\">\\n        A Safe Marketplace For All Consumers       \\n    </div>\");\n$(\"#content-right\").prepend(\"<img id=\\\"optimizely_874476019\\\" src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homepage-test-var1a.jpg\\\" />\");\n$(\"#optimizely_874476019\").css({\"height\":\"186\", \"width\":\"203\"});\n$(\"div > img\").removeAttr(\"src\").removeAttr(\"srcdoc\");\n$(\"div > img\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\"#optimizely_874476019\").css({\"border-width\":\"1px\"});\n$(\"#optimizely_874476019\").css({\"border-width\":\"1px\"});\n$(\"#optimizely_874476019\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/homepage-test-var1a.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" border=\\\"0\\\" width=\\\"203\\\">\");",
                "name": "Variation #2"
            },
            "78400210": {"name": "Variation #1"},
            "198286960": {"name": "Variation #1"},
            "76272040": {"name": "Original Page"},
            "173778034": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:30px;padding-top:10px;\\\">Looking for Reliable Appliances?  We got you covered...</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/appliances-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">When you are looking for a new appliance you'll want to know your new appliance has the features you are looking for, but you will also want to know how reliable it is.  We can help!</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Product performance is tested in our labs, but get reliability data from consumers like you who respond to our yearly reliability survey.</p></li>\\n                <li><p>Performance and reliability data are combined to help you find the products that are more likely to work well and be trouble free.</p></li>\\n                <li><p>Price and reliability are not necessarily related.  Don't spend more on appliances then you need to.</p></li>\\n        \\n               </ul>\\n          \\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/appliances-test-var2.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports can tell you not only what products test the best, but which ones last the longest.\\n</p>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size: 30px; padding-top: 10px;\\\"><span style=\\\"font-size: x-large;\\\">Looking for Reliable Appliances?  We got you covered...</span></div>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Price and reliability are not necessarily related.  Don't spend more on electronics than you need to.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>Over one million consumers give feedback allowing us to rate brands for quality of service and for product reliability.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>Spend wisely on products that will last\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p><strong>Spend wisely on products that will last</strong>\\n</p>\");\n$(\"strong\").replaceWith(\"<strong>Spend wisely on products that will last.</strong>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>Price and reliability are not necessarily related.  Don't spend more on appliances than you need to.\\n</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/appliances/appliances_products.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/appliances/appliance_testing.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header > span\").replaceWith(\"<span style=\\\"font-size: x-large;\\\">Looking for Reliable Appliances?  We have you covered...</span>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>More than one million consumers give feedback, allowing us to rate brands for quality of service and for product reliability.\\n</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/appliances/appliances_products.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"strong\").replaceWith(\"<p>Spend wisely on products that will last.</p>\");",
                "name": "Variation #2"
            },
            "173774068": {"name": "Original"},
            "173747445": {"name": "Original"},
            "116816503": {"name": "Original Page"},
            "166140626": {
                "code": "/* _optimizely_redirect=http://web.consumerreports.org/ranges/v2/index.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'http://web.consumerreports.org/ranges/v2/index.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Variation #1"
            },
            "173709817": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">We Have the Latest Babies &amp; Kids Ratings &amp; Reviews</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/babies-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/babies-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">Expert Babies & Kids Product Ratings\\n</div>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about 1,000 consumer electronics products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>We purchase all the products we test at retail, just like you.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state of the art labs at our headquarters in New York.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>We test innovative, high-interest products as soon as they become available.\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about Babies & Kids products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test over 250 Babies &amp; Kids products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test more than 250 Babies &amp; Kids products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports tests more than 250 Babies &amp; Kids products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state-of-the-art labs at our headquarters in New York.\\n</p>\");",
                "name": "Variation #3"
            },
            "173755386": {
                "code": "$(\"#content > div:eq(1)\").replaceWith(\"<style>\\n    #mainContainer #content #copy .red-arrows li p{\\n        height:auto;\\n        width:auto;\\n        font-size:16px;\\n    }\\n    #mainContainer #content #copy .red-arrows li{\\n        padding:5px 0 6px 15px;\\n    }\\n    #mainContainer #terms{\\n        margin-left:20px;\\n    }\\n</style>\\n<div>\\n  <div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">We Have the Latest Appliance Product Ratings &amp; Reviews</div>\\n    <img src=\\\"http://www.consumerreports.org/cro/resources/streaming/mvt/appliances-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\" />\\n    <div style=\\\"width:650px;float:right;margin-right:20px;\\\">\\n        <p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY COPY</p>\\n        <div id=\\\"copy\\\" style=\\\"margin-top:-10px;\\\">\\n               <ul class=\\\"red-arrows\\\">\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n                <li><p>Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet Bullet</p></li>\\n               </ul>\\n        </div>\\n    </div>\\n</div>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"https://web.consumerreports.org/etc/designs/marketing/sem/offerpagetests/appliances-test-var3.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about 1,000 consumer electronics products every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\"#header\").replaceWith(\"<div id=\\\"header\\\" style=\\\"font-size:28px;padding-top:10px;\\\">Expert Appliance Ratings</div>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>We purchase all the products we test at retail, just like you.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state of the art labs at our headquarters in New York.\\n</p>\");\n$(\".red-arrows > li:eq(2) > p:eq(0)\").replaceWith(\"<p>We test innovative, high-interest products as soon as they become available.\\n</p>\");\n$(\".red-arrows > li:eq(3) > p:eq(0)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(3)\").css({\"display\":\"none\", \"visibility\":\"\"});\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state of the art labs at our headquarters in  New York.\\n</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test about appliances every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test over 1,500 appliances every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state of the art labs.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state of the art labs.\\n</p>\");\n$(\".red-arrows > li:eq(1) > p:eq(0)\").replaceWith(\"<p>We purchase all the products we test at retail, just like you.\\n</p>\");\n$(\"div > img\").replaceWith(\"<img src=\\\"http://cu-staging/CRO/Y_other_internal/marketing_subscribe_photos/appliances/appliances_products.jpg\\\" style=\\\"float:left;margin-top:10px;margin-left:10px;\\\" width=\\\"203\\\" border=\\\"0\\\">\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports test more than 1,500 appliances every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state-of-the- art labs.\\n</p>\");\n$(\".red-arrows > li:eq(0) > p:eq(0)\").replaceWith(\"<p>We rigorously test all products in our 50 state-of-the-art labs.\\n</p>\");\n$(\"#content > div:eq(1) > div:eq(1) > p:eq(0)\").replaceWith(\"<p style=\\\"font-size:20px !important;margin-top:10px;border-bottom:1px solid #DADADA;padding:5px 0px 10px 0;\\\">Consumer Reports tests more than 1,500 appliances every year.  We help you spend wisely by telling you which products perform the best.\\n</p>\");",
                "name": "Variation #3"
            },
            "83789051": {"name": "Variation #1"},
            "169492093": {
                "code": "/* _optimizely_redirect=https://ec.consumerreports.org/ec/cro/csd2five/order.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'https://ec.consumerreports.org/ec/cro/csd2five/order.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Billing-$5"
            },
            "169492094": {
                "code": "/* _optimizely_redirect=https://ec.consumerreports.org/ec/cro/csd3five/order.htm */\nvar query = window.location.search;\nquery = query.indexOf('?') == 0 ? query.substring(1) : query;\nvar redirectFirst = 'https://ec.consumerreports.org/ec/cro/csd3five/order.htm';\nvar redirectSecond = '';\nvar questionMark = query.length || redirectSecond.length ? '?' : '';\nvar ampersand = query.length && redirectSecond.length && redirectSecond[0] != '#' ? '&' : '';\nwindow.location.replace(redirectFirst + questionMark + query + ampersand + redirectSecond);",
                "name": "Conf-$5"
            },
            "196264443": {"name": "Original"}
        },
        "installation_verified": true,
        "experiments": {
            "166754177": {
                "variation_ids": ["166669958", "166140626"],
                "name": "SEM Range Landing Page Test 1/4/13",
                "enabled": true,
                "enabled_variation_ids": ["166669958", "166140626"],
                "variation_weights": {"166669958": 5000, "166140626": 5000},
                "conditions": [{
                    "not": true,
                    "type": "browser",
                    "values": ["android", "blackberry", "iphone", "windows phone"]
                }, {
                    "type": "url",
                    "values": [{"value": "http://web.consumerreports.org/ranges/v1/index.html", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "173777033": {
                "variation_ids": ["173774068", "173775063", "173760575", "173768374", "187493314"],
                "name": "Conversion Test - Health (#9)",
                "enabled_variation_ids": ["173774068", "173775063", "173760575", "173768374", "187493314"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "166156298": {
                "variation_ids": ["166411557", "166632589"],
                "name": "SEM Refrigerator Landing Page Test 1/4/13",
                "enabled": true,
                "enabled_variation_ids": ["166411557", "166632589"],
                "variation_weights": {"166632589": 5000, "166411557": 5000},
                "conditions": [{
                    "not": true,
                    "type": "browser",
                    "values": ["android", "blackberry", "iphone", "windows phone"]
                }, {
                    "type": "url",
                    "values": [{
                        "value": "http://web.consumerreports.org/refrigerators/v1/index.html",
                        "match": "simple"
                    }]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "173760530": {
                "variation_ids": ["173768329", "173750425", "173778034", "173755386"],
                "name": "Conversion Test- Appliances (#5)",
                "enabled_variation_ids": ["173768329", "173750425", "173778034", "173755386"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "169419412": {
                "variation_ids": ["169431353", "169425372", "169175935", "169492093", "169443229", "169492094", "169181961"],
                "name": "Fundraising Cross-Sell Test (1-11-13)",
                "enabled": true,
                "ignore": 80,
                "enabled_variation_ids": ["169431353", "169425372", "169175935", "169492093", "169443229", "169492094", "169181961"],
                "variation_weights": {
                    "169175935": 1429,
                    "169443229": 1429,
                    "169431353": 1429,
                    "169181961": 1426,
                    "169492094": 1429,
                    "169492093": 1429,
                    "169425372": 1429
                },
                "conditions": [{
                    "not": true,
                    "type": "query",
                    "values": [{"value": "S", "key": "EXTKEY"}]
                }, {
                    "not": true,
                    "type": "referrer",
                    "values": [{
                        "value": "http://www.consumerreports.org/cro/cars/",
                        "match": "substring"
                    }, {
                        "value": "http://www.consumerreports.org/cro/index.htm",
                        "match": "exact"
                    }, {"value": "http://ec.consumerreports.org/ec/campaign.htm", "match": "substring"}]
                }, {
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "173748378": {
                "variation_ids": ["173719680", "173756322", "173700946", "173770122"],
                "name": "Conversion Test - Electronics (#4)",
                "enabled_variation_ids": ["173719680", "173756322", "173700946", "173770122"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "173704858": {
                "variation_ids": ["173762206", "173746820", "173767154", "173734600"],
                "name": "Conversion Test - Direct (#2)",
                "enabled": true,
                "enabled_variation_ids": ["173762206", "173746820", "173767154", "173734600"],
                "variation_weights": {"173762206": 2501, "173734600": 2500, "173767154": 2499, "173746820": 2500},
                "conditions": [{
                    "type": "code",
                    "value": "(!document.referrer || /^\\s*$/.test(document.referrer))"
                }, {
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "173768352": {
                "variation_ids": ["173700963", "173770142", "173731589", "173709817"],
                "name": "Conversion Test - Babies (#7)",
                "enabled_variation_ids": ["173700963", "173770142", "173731589", "173709817"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "125804593": {
                "variation_ids": ["125773506", "125797593"],
                "name": "CBDP Cross-Sell (Cars Referral)",
                "enabled_variation_ids": ["125773506", "125797593"],
                "conditions": [{
                    "type": "referrer",
                    "values": [{"value": "http://www.consumerreports.org/cro/cars/", "match": "substring"}]
                }, {
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "166453810": {
                "variation_ids": ["166562638", "166732320"],
                "name": "SEM Dishwasher Landing Page Test 1/4/13",
                "enabled": true,
                "enabled_variation_ids": ["166562638", "166732320"],
                "variation_weights": {"166732320": 5000, "166562638": 5000},
                "conditions": [{
                    "not": true,
                    "type": "browser",
                    "values": ["android", "blackberry", "iphone", "windows phone"]
                }, {
                    "type": "url",
                    "values": [{"value": "http://web.consumerreports.org/dishwashers/v1/index.html", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "173700958": {
                "variation_ids": ["173747445", "173746856", "173758535", "173723715"],
                "name": "Conversion Test - HomeGarden (#6)",
                "enabled_variation_ids": ["173747445", "173746856", "173758535", "173723715"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "173666998": {
                "variation_ids": ["173733552", "173775055", "173778086", "173768358"],
                "name": "Conversion Test - Money (#8)",
                "enabled_variation_ids": ["173733552", "173775055", "173778086", "173768358"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "78362424": {
                "variation_ids": ["78394467", "78400210"],
                "name": "Consumer Home Page",
                "enabled_variation_ids": ["78394467", "78400210"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "http://www.consumerreports.org/cro/index.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "175196605": {
                "variation_ids": ["175269381", "175050824"],
                "name": "Computers Page Sample Test",
                "enabled_variation_ids": ["175269381", "175050824"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "http://www.consumerreports.org/cro/computers.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "166509893": {
                "variation_ids": ["166111625", "166494211"],
                "name": "SEM Branded landing page test 1/4/13",
                "enabled": true,
                "enabled_variation_ids": ["166111625", "166494211"],
                "variation_weights": {"166111625": 5000, "166494211": 5000},
                "conditions": [{
                    "not": true,
                    "type": "browser",
                    "values": ["android", "blackberry", "iphone", "windows phone"]
                }, {
                    "type": "url",
                    "values": [{"value": "http://web.consumerreports.org/test/SEM/version5.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "169246669": {
                "variation_ids": ["169284230", "169246670"],
                "name": "CBDP Cross-Sell (Cars Referral - 1/11/13)",
                "enabled": true,
                "enabled_variation_ids": ["169284230", "169246670"],
                "variation_weights": {"169284230": 5000, "169246670": 5000},
                "conditions": [{
                    "not": true,
                    "type": "query",
                    "values": [{"value": "S", "key": "EXTKEY"}]
                }, {
                    "type": "referrer",
                    "values": [{"value": "http://www.consumerreports.org/cro/cars/", "match": "substring"}]
                }, {
                    "not": true,
                    "type": "referrer",
                    "values": [{
                        "value": "http://www.consumerreports.org/cro/index.htm",
                        "match": "exact"
                    }, {"value": "http://ec.consumerreports.org/ec/campaign.htm", "match": "substring"}]
                }, {
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "166636753": {
                "variation_ids": ["166704074", "166698317"],
                "name": "SEM Washing Machine Landing Page Test 1/4/13",
                "enabled": true,
                "enabled_variation_ids": ["166704074", "166698317"],
                "variation_weights": {"166704074": 5000, "166698317": 5000},
                "conditions": [{
                    "not": true,
                    "type": "browser",
                    "values": ["android", "blackberry", "iphone", "windows phone"]
                }, {
                    "type": "url",
                    "values": [{
                        "value": "http://web.consumerreports.org/washingmachines/v1/index.html",
                        "match": "simple"
                    }]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "196291284": {
                "variation_ids": ["196264443", "196253968"],
                "name": "CRO Save Page Test 3-2013",
                "enabled_variation_ids": ["196264443", "196253968"],
                "conditions": [{
                    "type": "url",
                    "values": [{
                        "value": "http://ec.consumerreports.org/ec/myaccount/cancel_subscription.htm?subscriptionId=19359623&actionType=subscription",
                        "match": "simple"
                    }]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "83696600": {
                "variation_ids": ["83552873", "83789051"],
                "name": "SEM Branded (Test)",
                "enabled_variation_ids": ["83552873", "83789051"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "http://web.consumerreports.org/test/SEM/version5.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "76225628": {
                "variation_ids": ["76272040", "76274047"],
                "name": "Simple Light Box Demo",
                "enabled_variation_ids": ["76272040", "76274047"],
                "conditions": [{
                    "type": "url",
                    "values": [{
                        "value": "http://www.consumerreports.org/cro/vacuum-cleaner-reviews.htm",
                        "match": "simple"
                    }]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}],
                "css": "#facebox {\n \n    /* overlay is hidden before loading */\n    display:none;\n \n    /* standard decorations */\n    width:400px;\n    border:10px solid #666;\n \n    /* for modern browsers use semi-transparent color on the border. nice! */\n    border:10px solid rgba(82, 82, 82, 0.698);\n \n    /* hot CSS3 features for mozilla and webkit-based browsers (rounded borders) */\n    -moz-border-radius:8px;\n    -webkit-border-radius:8px;\n  }\n \n  #facebox div {\n    padding:10px;\n    border:1px solid #3B5998;\n    background-color:#fff;\n    font-family:\"lucida grande\",tahoma,verdana,arial,sans-serif\n  }\n \n  #facebox h2 {\n    margin:-11px;\n    margin-bottom:0px;\n    color:#fff;\n    background-color:#6D84B4;\n    padding:5px 10px;\n    border:1px solid #3B5998;\n    font-size:20px;\n  }"
            },
            "173772126": {
                "variation_ids": ["173735519", "173739445", "173721736", "173766324"],
                "name": "Conversion Test - Autos (#3)",
                "enabled_variation_ids": ["173735519", "173739445", "173721736", "173766324"],
                "conditions": [{
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "173760487": {
                "variation_ids": ["173757961", "173768285", "173761257", "173705823"],
                "name": "Conversion Test - Homepage (#1)",
                "enabled": true,
                "enabled_variation_ids": ["173757961", "173768285", "173761257", "173705823"],
                "variation_weights": {"173761257": 2499, "173757961": 2501, "173768285": 2500, "173705823": 2500},
                "conditions": [{
                    "type": "query",
                    "values": [{"value": "I0AHAT8", "key": "INTKEY"}, {
                        "value": "I0AHLT4",
                        "key": "INTKEY"
                    }, {"value": "I0AHATC", "key": "INTKEY"}]
                }, {"not": true, "type": "query", "values": [{"value": "S", "key": "EXTKEY"}]}, {
                    "type": "referrer",
                    "values": [{"value": "http://www.consumerreports.org/cro/index.htm", "match": "simple"}]
                }, {
                    "not": true,
                    "type": "referrer",
                    "values": [{"value": "http://ec.consumerreports.org/ec/campaign.htm", "match": "substring"}]
                }, {
                    "type": "url",
                    "values": [{"value": "https://ec.consumerreports.org/ec/cro/order.htm", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "198259693": {
                "variation_ids": ["198364928", "198286960"],
                "name": "SEO Page",
                "enabled_variation_ids": ["198364928", "198286960"],
                "conditions": [{
                    "type": "url",
                    "values": [{
                        "value": "http://www.consumerreports.org/cro/air-conditioner-reviews.htm?EXTKEY=I15SBP0",
                        "match": "simple"
                    }]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "116677693": {
                "variation_ids": ["116816503", "116686635"],
                "name": "Simple Light Box Demo (1)",
                "enabled_variation_ids": ["116816503", "116686635"],
                "conditions": [{
                    "type": "url",
                    "values": [{
                        "value": "http://www.consumerreports.org/cro/vacuum-cleaner-reviews.htm",
                        "match": "simple"
                    }]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}],
                "css": "#facebox {\n \n    /* overlay is hidden before loading */\n    display:none;\n \n    /* standard decorations */\n    width:400px;\n    border:10px solid #666;\n \n    /* for modern browsers use semi-transparent color on the border. nice! */\n    border:10px solid rgba(82, 82, 82, 0.698);\n \n    /* hot CSS3 features for mozilla and webkit-based browsers (rounded borders) */\n    -moz-border-radius:8px;\n    -webkit-border-radius:8px;\n  }\n \n  #facebox div {\n    padding:10px;\n    border:1px solid #3B5998;\n    background-color:#fff;\n    font-family:\"lucida grande\",tahoma,verdana,arial,sans-serif\n  }\n \n  #facebox h2 {\n    margin:-11px;\n    margin-bottom:0px;\n    color:#fff;\n    background-color:#6D84B4;\n    padding:5px 10px;\n    border:1px solid #3B5998;\n    font-size:20px;\n  }"
            },
            "166463984": {
                "variation_ids": ["166789017", "166777183"],
                "name": "SEM Vacuum Landing Page Test 1/4/13",
                "enabled": true,
                "enabled_variation_ids": ["166789017", "166777183"],
                "variation_weights": {"166777183": 5000, "166789017": 5000},
                "conditions": [{
                    "not": true,
                    "type": "browser",
                    "values": ["android", "blackberry", "iphone", "windows phone"]
                }, {
                    "type": "url",
                    "values": [{"value": "http://web.consumerreports.org/vacuums/v2/index.html", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            },
            "166454910": {
                "variation_ids": ["166800039", "166776070"],
                "name": "SEM TVs Landing Page Test 1/4/13",
                "enabled": true,
                "enabled_variation_ids": ["166800039", "166776070"],
                "variation_weights": {"166800039": 5000, "166776070": 5000},
                "conditions": [{
                    "not": true,
                    "type": "browser",
                    "values": ["android", "blackberry", "iphone", "windows phone"]
                }, {
                    "type": "url",
                    "values": [{"value": "http://web.consumerreports.org/tvs/v2/index.html", "match": "simple"}]
                }, {"only_first_time": true, "type": "visitor", "value": "all"}]
            }
        },
        "click_goals": [{
            "event_name": "custom_event",
            "experiments": [78362424],
            "selector": "a"
        }, {"event_name": "bb_button_click", "experiments": [101028200], "selector": ".hideImageOnPrint > img"}],
        "version": "www-master-1049.365890886373341652",
        "admin_account_id": 62715456,
        "www_host": "www.optimizely.com",
        "project_id": 69071259,
        "segments": {
            "191143264": {
                "name": "iPhone",
                "api_name": "iphone",
                "add_condition": [{"type": "browser", "values": ["iphone"]}, {
                    "only_first_time": true,
                    "type": "visitor",
                    "value": "all"
                }],
                "id": 191143264
            },
            "191085326": {
                "name": "iPads Segments",
                "api_name": "ipads_segments",
                "add_condition": [{"type": "browser", "values": ["ipad"]}, {
                    "only_first_time": true,
                    "type": "visitor",
                    "value": "all"
                }],
                "id": 191085326
            },
            "170962448": {
                "name": "Campaign",
                "segment_value_type": "campaign",
                "api_name": "optimizely_campaign",
                "id": 170962448
            },
            "172047966": {
                "name": "Source Type",
                "segment_value_type": "source_type",
                "api_name": "optimizely_source_type",
                "id": 172047966
            },
            "172431806": {
                "name": "Mobile Visitors",
                "segment_value_type": "mobile",
                "api_name": "optimizely_mobile",
                "id": 172431806
            },
            "172047967": {
                "name": "Browser",
                "segment_value_type": "browser",
                "api_name": "optimizely_browser",
                "id": 172047967
            }
        },
        "revision": 466
    };

    var optly = {nativity: {}};
    optly.nativity.getNativeGetElementsByClassName = function () {
        var a = document.getElementsByClassName;
        if (!optly.nativity.isNativeFunction(a))var a = (window.optimizely || {}).getElementsByClassName, b = (window.optly || {}).getElementsByClassName, a = optly.nativity.isNativeFunction(a) ? a : optly.nativity.isNativeFunction(b) ? b : null;
        return a
    };
    optly.nativity.isNativeFunction = function (a) {
        return a && -1 !== String(a).indexOf("[native code]")
    };
    optly.Cleanse = {};
    optly.Cleanse.each = function (a, b, c) {
        var g = !!Object.prototype.__lookupGetter__, e = !!Object.prototype.__lookupSetter__, f;
        for (f in a)if (a.hasOwnProperty(f)) {
            var d = g ? a.__lookupGetter__(f) : null, h = e ? a.__lookupSetter__(f) : null;
            try {
                b.call(c, f, !d ? a[f] : null, d, h)
            } catch (i) {
            }
        }
    };
    optly.Cleanse.finish = function () {
        if (optly.Cleanse.running) {
            optly.Cleanse.running = !1;
            optly.Cleanse.each(optly.Cleanse.types, function (a, b) {
                Object.prototype.__defineGetter__ && optly.Cleanse.each(optly.Cleanse.getters[a], function (c, d) {
                    b.prototype.__defineGetter__(c, d);
                    optly.Cleanse.log("restored getter", a, c)
                });
                Object.prototype.__defineSetter__ && optly.Cleanse.each(optly.Cleanse.setters[a], function (c, d) {
                    b.prototype.__defineSetter__(c, d);
                    optly.Cleanse.log("restored setter", a, c)
                });
                optly.Cleanse.each(optly.Cleanse.properties[a],
                    function (c, d) {
                        b.prototype[c] = d;
                        optly.Cleanse.log("restored property", a, c)
                    })
            });
            optly.Cleanse.unfixGetElementsByClassName();
            optly.Cleanse.log("finish");
            var a = window.console;
            if ((-1 !== window.location.hash.indexOf("optimizely_log=true") || -1 !== window.location.search.indexOf("optimizely_log=true")) && a && a.log)for (var b = optly.Cleanse.logs, c = 0; c < b.length; c++)a.log(b[c])
        }
    };
    optly.Cleanse.log = function (a, b, c) {
        b ? (b = b.replace(/_/g, ""), optly.Cleanse.logs.push("Optimizely / Info / Cleanse / " + a + ": " + b + "." + c)) : optly.Cleanse.logs.push("Optimizely / Info / Cleanse / " + a)
    };
    optly.Cleanse.start = function () {
        var a = window.location.hostname;
        if (!(-1 !== a.indexOf("optimizely") && -1 === a.indexOf("edit") && -1 === a.indexOf("preview") && -1 === a.indexOf("test")))if (optly.Cleanse.running)optly.Cleanse.log("already running so didn't start"); else {
            optly.Cleanse.log("start");
            optly.Cleanse.running = !0;
            for (var b in optly.Cleanse.types)optly.Cleanse.types[b] || delete optly.Cleanse.types[b];
            optly.Cleanse.each(optly.Cleanse.types, function (a, b) {
                optly.Cleanse.getters[a] = {};
                optly.Cleanse.properties[a] =
                {};
                optly.Cleanse.setters[a] = {};
                optly.Cleanse.each(b.prototype, function (e, f, d, h) {
                    optly.nativity.isNativeFunction(f) || optly.nativity.isNativeFunction(d) || optly.nativity.isNativeFunction(h) ? optly.Cleanse.log("ignore native code", a, e) : (d ? (optly.Cleanse.getters[a][e] = d, optly.Cleanse.log("cleansed getter", a, e)) : (optly.Cleanse.properties[a][e] = f, optly.Cleanse.log("cleansed property", a, e)), h && (optly.Cleanse.setters[a][e] = h, optly.Cleanse.log("cleansed setter", a, e)), delete b.prototype[e])
                })
            });
            optly.Cleanse.fixGetElementsByClassName();
            optly.Cleanse.hasRunStart = !0
        }
    };
    optly.Cleanse.fixGetElementsByClassName = function () {
        if (!optly.nativity.isNativeFunction(document.getElementsByClassName)) {
            var a = optly.nativity.getNativeGetElementsByClassName();
            a ? (optly.Cleanse.getElementsByClassName = document.getElementsByClassName, document.getElementsByClassName = a) : optly.Cleanse.log("Error: native HTMLElement.prototype.getElementsByClassName missing")
        }
    };
    optly.Cleanse.unfixGetElementsByClassName = function () {
        optly.Cleanse.getElementsByClassName && (document.getElementsByClassName = optly.Cleanse.getElementsByClassName, optly.Cleanse.getElementsByClassName = null)
    };
    optly.Cleanse.getElementsByClassName = null;
    optly.Cleanse.getters = {};
    optly.Cleanse.logs = [];
    optly.Cleanse.properties = {};
    optly.Cleanse.setters = {};
    optly.Cleanse.types = {HTMLElement_: window.HTMLElement, Object_: Object};
    window.optly = window.optly || {};
    window.optly.Cleanse = {finish: optly.Cleanse.finish, logs: optly.Cleanse.logs, start: optly.Cleanse.start};
    optly.domain = {};
    optly.domain.check = function (a) {
        for (var b = 0, c = optly.domain.BLACK_LIST.length; b < c; b++) {
            var g = optly.domain.BLACK_LIST[b];
            if (g.test(a))return !1
        }
        b = 0;
        for (c = optly.domain.WHITE_LIST.length; b < c; b++)if (g = optly.domain.WHITE_LIST[b], g.test(a))return !0;
        return !1
    };
    optly.domain.BLACK_LIST = [/(edit|preview)(-local)?\.optimizely\.com/, /optimizelyedit\.appspot\.com/];
    optly.domain.WHITE_LIST = [/^(https:)?\/\/www\.local\//, /^(https:)?\/\/([A-Za-z0-9\-]+\.)?optimizely\.(com|test)\//, /^(https:)?\/\/([A-Za-z0-9\-]+\.)?optimizely(-hrd)?\.appspot\.com\//];
    optly.Cleanse.start();
    var $ = jQuery;
    var h = void 0, i = !0, j = null, k = !1;

    function aa(a, b, c) {
        switch (c) {
            case "exact":
                return a = ba(a), a = ca(a, "optimizely_log"), a = ca(a, "optimizely_verbose"), a === ba(b);
            case "regex":
                try {
                    return Boolean(a.match(b))
                } catch (d) {
                    return k
                }
            case "simple":
                return a = ba(da(a)), b = ba(da(b)), a === b;
            case "substring":
                return a = ba(a, i), b = ba(b, i), -1 !== a.indexOf(b);
            default:
                return k
        }
    }

    function da(a) {
        var b = a.indexOf("?");
        -1 !== b && (a = a.substring(0, b));
        b = a.indexOf("#");
        -1 !== b && (a = a.substring(0, b));
        return a
    }

    function ba(a, b) {
        var a = a.toLowerCase().replace(/[/&?]+$/, ""), c = ea.slice(0);
        b || (c = c.concat(fa));
        for (var d = c.length, e = 0; e < d; e++)a = a.replace(RegExp("^" + c[e]), "");
        return a
    }

    function ca(a, b) {
        return a.replace("&" + b + "=true", "").replace("?" + b + "=true&", "?").replace("?" + b + "=true", "")
    }

    var ea = ["https?://edit.local/", "https?://edit.optimizely.com/", "https?://.*?.?optimizelyedit.appspot.com/", "https?://preview.optimizely.com/", "https?://"], fa = ["www."];

    function ga(a) {
        a = a || {};
        if (m) {
            a && a.sVariable && (ha = a.sVariable);
            var b = ha || ("undefined" !== typeof window.s ? window.s : j);
            if (b)if (ia) {
                if ((a = ja) && b)try {
                    o("Integrator", "Fixing SiteCatalyst referrer to %s", a), b.referrer = String(a)
                } catch (c) {
                    o("Integrator", "Error setting SiteCatalyst referrer: %s", c)
                }
                o("Integrator", "Tracking with SiteCatalyst");
                p(ka(), function (a) {
                    var c = q(a), a = r(c, a, 100, 100, 25, i), f = a.key + ": " + a.value, a = [], g = u(c, "site_catalyst_evar") || j, c = u(c, "site_catalyst_prop") || j;
                    g !== j && a.push("eVar" + g);
                    c !==
                    j && a.push("prop" + c);
                    p(a, function (a) {
                        o("Integrator", "Setting SiteCatalyst %s='%s'", a, f);
                        b[a] = f
                    })
                })
            } else la = i; else v("Integrator", "Error with SiteCatalyst integration: 's' variable not defined")
        }
    }

    function ma() {
        if (m) {
            var a = y("optimizelyReferrer");
            a && 0 < a.length && (ja = a, z("optimizelyReferrer", ""));
            if (a = ja)try {
                o("Integrator", "Fixing _gaq._setReferrerOverride with %s", a), _gaq.push(["_setReferrerOverride", a])
            } catch (b) {
                o("Integrator", "Error setting Google Analytics referrer: %s", b)
            }
            p(ka(), function (a) {
                var b = q(a);
                if (u(b, "chartbeat")) {
                    var c = na;
                    na = "";
                    var g = r(b, a, 10, 10, 5, k);
                    na = c;
                    c = oa(a);
                    pa = g.key + ": " + String(c);
                    try {
                        o("Integrator", "Calling _cbq.push");
                        _cbq.push(["_optlyx", pa])
                    } catch (l) {
                        v("Integrator", "Error sending Chartbeat data for " +
                            A(b))
                    }
                }
                if (u(b, "crazyegg")) {
                    g = r(b, a, 100, 100, 15, k);
                    try {
                        o("Integrator", "Defining CE_SNAPSHOT_NAME");
                        window.CE_SNAPSHOT_NAME = g.key + ": " + g.value
                    } catch (t) {
                        v("Integrator", "Error sending CrazyEgg data for " + A(b))
                    }
                }
                if (qa(b)) {
                    g = qa(b);
                    c = 0;
                    B(g) && (c = g.slot || c);
                    var g = c, c = qa(b), n = "";
                    B(c) && (n = c.tracker || n);
                    c = n;
                    n = r(b, a, 28, 24, 5, i);
                    try {
                        var w = "";
                        c !== "" && (w = c + ".");
                        o("Integrator", "Calling _gaq._setCustomVar for slot %d and scope %d", g, sa);
                        _gaq.push([w + "_setCustomVar", g, n.key, n.value, sa])
                    } catch (Q) {
                        v("Integrator", "Error sending Google Analytics data for " +
                            A(b))
                    }
                }
                if (C("kissmetrics")) {
                    g = r(b, a, 100, 100, 15, i);
                    c = {};
                    c[g.key] = g.value;
                    try {
                        o("Integrator", "Calling _kmq.set");
                        _kmq.push(["set", c])
                    } catch (L) {
                        v("Integrator", "Error sending KISSmetrics data for " + A(b))
                    }
                }
                if (u(b, "mixpanel")) {
                    a = r(b, a, 100, 100, 15, k);
                    g = {};
                    g[a.key] = a.value;
                    try {
                        o("Integrator", "Calling mixpanel.push");
                        mixpanel.push(["register", g])
                    } catch (ra) {
                        v("Integrator", "Error sending Mixpanel data for " + A(b))
                    }
                }
            });
            a = y("optimizelyChartbeat") || "";
            try {
                if (a && pa != a && (o("Integrator", "Calling _cbq.push for referral"),
                        _cbq.push(["_optlyr", a])), pa != a)o("Integrator", "Set new Chartbeat referral cookie."), z("optimizelyChartbeat", pa)
            } catch (c) {
                v("Integrator", "Error sending Chartbeat referral for " + a)
            }
            ia = i;
            la && (ga(), la = k)
        }
    }

    function ta(a, b) {
        return a.replace(/[^a-zA-Z0-9\.\~\!\*\(\)\']+/g, "_").substring(0, b)
    }

    function ka() {
        var a = ua.concat(D), b = [];
        p(va(), function (c) {
            var d = q(c), e = k;
            if (wa(d)) {
                var f = xa(c);
                E(a, d) && (o("Integrator", '"%s" relevant because experiment active', f), e = i);
                ya(za(c)) && (o("Integrator", '"%s" relevant because it redirects', f), e = i);
                e && b.push(c)
            }
        });
        return b
    }

    function r(a, b, c, d, e, f) {
        a = na + (u(a, "name") || "");
        b = Aa(b);
        1 < b.length ? (b = $.map(b, function (a) {
            return a.substr(0, e - 1)
        }), b = b.join("~")) : b = b[0];
        f ? (a = ta(a, c), b = ta(b.replace("#", ""), d)) : (a = a.substring(0, c), b = b.substring(0, d));
        return {key: a, value: b}
    }

    var la = k, pa = "", sa = 2, ia = k, na = "Optimizely ", ha = j, ja = j;

    function Ba(a, b, c) {
        Ca = i;
        F && c !== i && G.e(document.location.href);
        a = String(a);
        b = String(b);
        if ("-1" === b) {
            H[a] && delete H[a];
            Da[a] && delete Da[a];
            for (c = 0; c < I.length; c++)I[c].j === a && I.splice(c, 1);
            Ea()
        } else {
            c = j;
            if (256 >= Number(b)) {
                var c = String, d = Fa(a), e = j;
                try {
                    e = d[b]
                } catch (f) {
                }
                c = c(e)
            } else c = String(b);
            if ((b = K(a)) && 0 < b.length) {
                a:{
                    b = K(a);
                    for (d = 0; d < b.length; d++)if (e = Ga(b[d]), E(e, c)) {
                        b = b[d];
                        break a
                    }
                    b = ""
                }
                Ha[a] = Ha[a] || {};
                Ha[a][b] = c;
                v("Distributor", "Preferring variation partial " + c + " of section " + b + " of experiment " +
                    a);
                a = Ia(a);
                1 === a.length && Ja(a[0], "api.bucketUser", k, i)
            } else Ja(c, "api.bucketUser", k, i)
        }
        Ka()
    }

    function La(a) {
        a && "tracking" === a || (F = k);
        m = k
    }

    function Na() {
        Oa = {};
        M = {};
        Pa = {};
        p(va(), function (a) {
            var b = q(a);
            Oa[b] = a.split("_");
            M[b] = oa(a);
            Pa[b] = xa(a)
        });
        N = {experiments: {}, sections: {}, segments: {}, state: {}, variations: {}, visitor: {}};
        for (var a = Qa(), b = 0; b < a.length; b++) {
            var c = a[b], d = {};
            d.code = u(c, "code") || "";
            d.name = u(c, "name") || "";
            d.manual = Ra(c);
            d.section_ids = K(c);
            d.variation_ids = Fa(c);
            N.experiments[c] = d
        }
        a = Sa();
        for (b = 0; b < a.length; b++)c = a[b], N.segments[c] = {name: Ta(c, "name") || ""};
        a = Ua(C("sections") || {});
        for (b = 0; b < a.length; b++)c = a[b], d = {}, d.name = C("sections",
                c, "name") || "", d.variation_ids = Ga(c), N.sections[c] = d;
        a = Ua(C("variations") || {});
        for (b = 0; b < a.length; b++)c = a[b], d = {}, d.name = xa(c), d.code = za(c), N.variations[c] = d;
        a = {};
        b = Va();
        a.browser = {
                ff: "Firefox",
                ie: "Internet Explorer",
                safari: "Safari",
                gc: "Google Chrome",
                opera: "Opera"
            }[b] || "";
        b = Wa();
        a.location = {city: b.city, continent: b.continent, country: b.country, region: b.region};
        a.params = {};
        c = Xa();
        c.reverse();
        b = 0;
        for (d = c.length; b < d; b++)a.params[c[b][0]] = decodeURIComponent(c[b][1]);
        a.referrer = String(document.referrer);
        a.segments = Ya();
        a.mobile = "unknown" !== Za();
        a.os = $a();
        N.visitor = a;
        b = {};
        b.activeExperiments = ua || [];
        b.variationIdsMap = Oa;
        b.variationMap = M;
        b.variationNamesMap = Pa;
        N.state = b;
        ab(window.optimizely, {
            activeExperiments: ua,
            allExperiments: bb(),
            all_experiments: bb(),
            data: N,
            variationIdsMap: Oa,
            variationMap: M,
            variationNamesMap: Pa,
            variation_map: M
        })
    }

    var N = {}, cb = [], db = [], Oa = {}, M = {}, Pa = {};

    function eb(a, b) {
        var b = b === i, c, d = j;
        p(I, function (b) {
            a == b.j && (d = b.id)
        });
        if ((c = d) && 0 < c.length)return v("Distributor", "Not distributing experiment " + a + " (already in plan)"), i;
        if (b || a in H)return v("Distributor", "Not distributing experiment " + a + " (is ignored)"), O[a] = "it is ignored", k;
        c = u(a, "enabled_variation_ids") || [];
        if (0 === c.length)return v("Distributor", "Permanently ignoring experiment " + a + " (no enabled variations)"), O[a] = "it has no enabled variations", fb(a), k;
        var e = u(a, "ignore") || 0;
        if (e > Math.floor(100 *
                Math.random()))return v("Distributor", "Permanently ignoring experiment " + a + "(" + e + "% likelihood)"), fb(a), k;
        e = c;
        Ha[a] !== h && (v("Distributor", "Taking into account bucketUser variations for experiment " + a), e = Ia(a));
        var f;
        f = e;
        var g = [], l = u(a, "variation_weights") || {};
        p(f, function (a) {
            g.push(l[a])
        });
        f = gb(g);
        e = e[f];
        v("Distributor", "Picked variation " + e + " [index " + f + " of " + c.length + "]");
        Ja(e, "distributor", k);
        return i
    }

    function Ia(a) {
        var b = [];
        p(u(a, "enabled_variation_ids") || [], function (c) {
            var d = i, e;
            for (e in Ha[a])-1 === c.indexOf(Ha[a][e]) && (d = k);
            d && b.push(c)
        });
        return b
    }

    var Ha = {};

    function Xa() {
        var a = window.location.search || "";
        0 === a.indexOf("?") && (a = a.substring(1));
        for (var a = a.split("&"), b = [], c = 0; c < a.length; c++) {
            var d = "", e = "", f = a[c].split("=");
            0 < f.length && (d = f[0]);
            1 < f.length && (e = f[1]);
            b.push([d, e])
        }
        return b
    }

    function hb(a) {
        for (var b = Xa(), c = 0; c < b.length; c++) {
            var d = b[c];
            if (d[0] === a)return d[1]
        }
        return j
    };
    function ib(a) {
        return a && -1 !== String(a).indexOf("[native code]")
    };
    function ya(a) {
        return -1 !== a.indexOf("_optimizely_redirect")
    }

    function jb(a) {
        return -1 !== a.indexOf("_optimizely_redirect_no_cookie")
    }

    function kb(a) {
        return jb(a || "") ? i : !y("optimizelyRedirect")
    };
    function lb() {
        var a = navigator.userAgent, b = mb([{id: "gc", substring: "Chrome", h: "Chrome"}, {
            id: "safari",
            O: navigator.vendor,
            substring: "Apple",
            h: "Version"
        }, {id: "ff", substring: "Firefox", h: "Firefox"}, {id: "opera", prop: window.opera, h: "Opera"}, {
            id: "ie",
            substring: "MSIE",
            h: "MSIE"
        }], a), c = mb([{id: "android", substring: "Android"}, {id: "blackberry", substring: "BlackBerry"}, {
            id: "ipad",
            substring: "iPad"
        }, {id: "iphone", substring: "iPhone"}, {id: "ipod", substring: "iPod"}, {
            id: "windows phone",
            substring: "Windows Phone"
        }], a), d = j, e =
            b.h;
        e && (d = nb(a, e) || nb(navigator.appVersion, e));
        return {A: b.id || "unknown", B: d || "unknown", L: c.id || "unknown"}
    }

    function nb(a, b) {
        var c = a.indexOf(b), d = j;
        -1 !== c && (c += b.length + 1, d = parseFloat(a.substring(c)));
        return d
    }

    function mb(a, b) {
        return p(a, function (a) {
                var d = a.O || b;
                if (d && -1 !== d.indexOf(a.substring) || a.prop)return a
            }) || {}
    };
    var ob = 0, F = i, pb = j, P = "", qb = k, R = k, rb = k, Ca = k, sb = k, m = i;
    var tb, ub;
    (function () {
        function a(a) {
            d.lastIndex = 0;
            return d.test(a) ? '"' + a.replace(d, function (a) {
                var b = g[a];
                return "string" === typeof b ? b : "\\u" + ("0000" + a.charCodeAt(0).toString(16)).slice(-4)
            }) + '"' : '"' + a + '"'
        }

        function b(c, d) {
            var g, Q, L, ra, Ma = e, J, x = d[c];
            "function" === typeof l && (x = l.call(d, c, x));
            switch (typeof x) {
                case "string":
                    return a(x);
                case "number":
                    return isFinite(x) ? String(x) : "null";
                case "boolean":
                case "null":
                    return String(x);
                case "object":
                    if (!x)return "null";
                    e += f;
                    J = [];
                    if ("[object Array]" === Object.prototype.toString.apply(x)) {
                        ra = x.length;
                        for (g = 0; g < ra; g += 1)J[g] = b(g, x) || "null";
                        L = 0 === J.length ? "[]" : e ? "[\n" + e + J.join(",\n" + e) + "\n" + Ma + "]" : "[" + J.join(",") + "]";
                        e = Ma;
                        return L
                    }
                    if (l && "object" === typeof l) {
                        ra = l.length;
                        for (g = 0; g < ra; g += 1)"string" === typeof l[g] && (Q = l[g], (L = b(Q, x)) && J.push(a(Q) + (e ? ": " : ":") + L))
                    } else for (Q in x)Object.prototype.hasOwnProperty.call(x, Q) && (L = b(Q, x)) && J.push(a(Q) + (e ? ": " : ":") + L);
                    L = 0 === J.length ? "{}" : e ? "{\n" + e + J.join(",\n" + e) + "\n" + Ma + "}" : "{" + J.join(",") + "}";
                    e = Ma;
                    return L
            }
        }

        var c = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
            d = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, e, f, g = {
                "\b": "\\b",
                "\t": "\\t",
                "\n": "\\n",
                "\f": "\\f",
                "\r": "\\r",
                '"': '\\"',
                "\\": "\\\\"
            }, l;
        tb = function (a, c, d) {
            var g;
            f = e = "";
            if ("number" === typeof d)for (g = 0; g < d; g += 1)f += " "; else"string" === typeof d && (f = d);
            if ((l = c) && "function" !== typeof c && ("object" !== typeof c || "number" !== typeof c.length))throw Error("JSON.stringify");
            return b("", {"": a})
        };
        ub = function (a, b) {
            function d(a, c) {
                var e, f, g = a[c];
                if (g && "object" === typeof g)for (e in g)Object.prototype.hasOwnProperty.call(g, e) && (f = d(g, e), f !== h ? g[e] = f : delete g[e]);
                return b.call(a, c, g)
            }

            var e, a = String(a);
            c.lastIndex = 0;
            c.test(a) && (a = a.replace(c, function (a) {
                return "\\u" + ("0000" + a.charCodeAt(0).toString(16)).slice(-4)
            }));
            if (/^[\],:{}\s]*$/.test(a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, "@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]").replace(/(?:^|:|,)(?:\s*\[)+/g, "")))return e = eval("(" + a + ")"), "function" === typeof b ? d({"": e}, "") : e;
            throw new SyntaxError("JSON.parse");
        }
    })();
    var G = {
        w: function (a, b) {
            var c = {}, c = b && vb(b) ? {revenue: Number(b)} : b;
            G.e(a, "custom", c)
        }, e: function (a, b, c) {
            c = c || {};
            wb(a, c);
            m && (G.l.push({
                name: a,
                type: b,
                options: c
            }), G.v ? (G.r(), v("Tracker", "Tracking event '" + a + "'")) : v("Tracker", "Queued tracking event '" + a + "'"))
        }, F: function () {
            $("html").one("mousedown", xb(G.e, "engagement"))
        }, G: function (a) {
            return function () {
                G.M(a)
            }
        }, m: function () {
            var a = y("optimizelyPendingLogEvents") || "[]", b = [];
            try {
                b = ub(a)
            } catch (c) {
            }
            if (S(b))for (var a = 0, d = b.length; a < d; a++) {
                var e = b[a];
                if ("string" !== typeof e) {
                    b = [];
                    break
                } else try {
                    ub(e);
                    b = [];
                    break
                } catch (f) {
                }
            } else b = [];
            return b
        }, K: function (a, b) {
            var c = [yb("log_host")];
            C("use_staging_log") && c.push(yb("staging_log_host"));
            v("Tracker", "Making " + c.length + " log request(s).");
            for (var d = 0; d < c.length; d++)G.p(c[d] + "/event?" + a, b)
        }, o: function (a, b) {
            var c = new Image;
            c.onload = b;
            a = G.P(a);
            c.src = a;
            G.images.push(c)
        }, p: function (a, b) {
            if (G.I(a))try {
                var c = new XMLHttpRequest;
                if ("withCredentials"in c)c.onerror = b, c.onload = b, c.open("GET", a, i), c.withCredentials = i, c.send();
                else throw"CORS with credentials is not supported.";
            } catch (d) {
                v("Tracker", "Found that XHR with credentials is not supported in this browser."), G.o(a, b)
            } else G.o(a, b)
        }, I: function (a) {
            return -1 !== a.indexOf(G.k)
        }, P: function (a) {
            return a.replace("&" + G.k, "")
        }, q: function (a) {
            var b = (a = a === i || "true" === a) ? "true" : "false";
            a ? (z("optimizelyOptOut", b, 31536E4), z("optimizelyBuckets", b, 31536E4), alert("You have successfully opted out of Optimizely for this domain.")) : (z("optimizelyOptOut", b, 31536E4), alert("You are NOT opted out of Optimizely for this domain."))
        },
        M: function (a) {
            for (var b = G.m(), c = 0, d = b.length; c < d; c++)if (b[c] === a) {
                b.splice(c, 1);
                break
            }
            G.u(b);
            v("Tracker", "Removed a pending log event from the pending events cookie.")
        }, l: [], v: k, r: function () {
            var a = ["a=" + zb(), "d=" + Ab(), "y=" + !!C("ip_anonymization")];
            Ca && a.push("override=true");
            p(va(), function (b) {
                var c = q(b);
                a.push("x" + c + "=" + b)
            });
            a.push("f=" + Bb().join(","));
            p(Ya(), function (b, c) {
                a.push("s" + b + "=" + c)
            });
            var b = a.join("&"), c = [];
            p(G.l, function (a) {
                var b = [];
                a.name && b.push("n=" + encodeURIComponent(a.name));
                a.options.revenue &&
                b.push("v=" + encodeURIComponent(a.options.revenue));
                a.options.anonymous !== i && b.push("u=" + Cb());
                G.S(a) && b.push(G.k);
                b.push("t=" + +new Date);
                c.push(b.join("&"));
                if ("custom" === a.type)try {
                    G.R(a.name)
                } catch (d) {
                }
            });
            var d = c.concat(G.m());
            G.u(d);
            d = G.t ? c : d;
            G.t = i;
            for (var e = 0, f = d.length; e < f; e++) {
                var g = d[e];
                G.K(b + "&" + g, G.G(g))
            }
            G.l = [];
            G.v = i
        }, N: function () {
            if (m) {
                var a = "//" + C("www_host") + "/account/snippet_installed?project_id=" + zb() + "&wxhr=true";
                v("Tracker", "Making snippet verification request.");
                G.p(a, j)
            }
        }, u: function (a) {
            for (var b =
                tb(a); 1536 < b.length;)a = a.slice(0, -1), b = tb(a);
            z("optimizelyPendingLogEvents", b, 15)
        }, R: function (a) {
            var b = Cb(), c = y("optimizelyCustomEvents") || "{}";
            try {
                c = ub(c)
            } catch (d) {
                c = {}
            }
            var e = c[b] || (c[b] = []), e = S(e) ? e : [];
            -1 !== $.inArray(a, e) && e.splice($.inArray(a, e), 1);
            e.push(a);
            10 < e.length && e.shift();
            c[b] = e;
            var a = 0, e = j, f = 0, g;
            for (g in c)c.hasOwnProperty(g) && (a++, c[g].length > f && g !== b && (e = g, f = c[g].length));
            10 < a && e !== j && delete c[e];
            z("optimizelyCustomEvents", tb(c), 31536E4)
        }, S: function (a) {
            return "https:" === document.location.protocol &&
            "ie" === Va() && 10 <= Db() ? k : "pageview" === a.type
        }, images: [], t: k, k: "wxhr=true"
    };

    function Eb(a, b) {
        var c;
        c = $.trim(b);
        var d = "";
        if (window.optimizely && window.optimizely.data)if (d = c.match(Fb))d = window.optimizely.data.visitor.params[d[1]], d === h && (d = ""); else {
            for (var d = c.split("."), e = window.optimizely, f = 0, g = d.length; f < g; f++)if (e = e[d[f]], e === h || e === j) {
                e = "";
                break
            }
            d = "" + e
        }
        v("Template", c + " evaluated to: '" + d + "'");
        return d
    }

    var Gb = /\{\{ *optimizely\.([^\n\r{}<>]*)\}\}/g, Fb = /^data\.visitor\.params\.(.*)$/;

    function wb(a, b) {
        var b = b || {}, c = window.optimizelyPreview;
        c || (c = [], window.optimizelyPreview = c);
        Hb || (c.push(["addEvent", window.location.href, {type: "url"}]), Hb = i);
        c.push(["addEvent", a, b])
    }

    function Ib() {
        v("Preview", "Will load preview script");
        var a = ["https://", C("www_host"), "/js/preview-", C("version"), ".js?account_id=", Ab()].join("");
        v("Preview", "Now loading preview script " + a);
        Jb(a)
    }

    var Hb = k, Kb = k, Lb = [], O = {};
    var Mb = window.OPTIMIZELY_TEST_MODULE, Nb = "com local net org xxx edu es gov biz info fr nl ca de kr it me ly tv mx cn jp il in iq".split(" "), Ob = /\/\*\s*_optimizely_variation_url( +include="([^"]*)")?( +exclude="([^"]*)")?( +match_type="([^"]*)")?( +include_match_types="([^"]*)")?( +exclude_match_types="([^"]*)")?( +id="([^"]*)")?\s*\*\//;

    function y(a) {
        var b = a + "=", c = [];
        p((document.cookie || "").split(/\s*;\s*/), function (a) {
            0 === a.indexOf(b) && c.push(decodeURIComponent(a.substr(b.length)))
        });
        var d = c.length;
        1 < d && o("Cookie", "Values found for %s: %s", a, d);
        return 0 === d ? j : c[0]
    }

    function z(a, b, c) {
        var d = Pb || T || Qb, e = document.location.hostname;
        !T && C("remote_public_suffix") && Rb.push({z: c, name: a, value: b});
        d && (d === T && d !== Qb) && (Sb(a, e), Sb(a, Qb));
        Tb(a, b, d, c);
        var f = y(a);
        f === b ? o("Cookie", "Successful set %s=%s on %s", a, b, d) : (o("Cookie", "Setting %s on %s apparently failed (%s != %s)", a, d, f, b), o("Cookie", "Setting %s on %s", a, e), Tb(a, b, e, c), f = y(a), f === b && (o("Cookie", "Setting %s on %s worked; saving as new public suffix", a, e), Qb = e))
    }

    function Sb(a, b) {
        o("Cookie", "Deleting %s on %s", a, b);
        document.cookie = [a, "=; domain=.", b, "; path=/; expires=", (new Date(0)).toUTCString()].join("")
    }

    function Ub(a) {
        T = a.public_suffix;
        o("Cookie", "Public suffix request returned: %s", T);
        z("optimizelyPublicSuffix", T, 31536E4);
        if (T !== Qb)for (; 0 < Rb.length;)a = Rb.shift(), z(a.name, a.value, a.z);
        Rb = []
    }

    function Vb(a) {
        var a = yb("api_host") + "/iapi/public_suffix?host=" + encodeURIComponent(a), b = "callback" + Math.random().toString().replace("0.", ""), c = document, d = c.head || c.getElementsByTagName("head")[0] || c.documentElement, c = c.createElement("script");
        window.optimizely[b] = Ub;
        c.async = "async";
        c.src = [a, -1 !== a.indexOf("?") ? "&" : "?", "callback=optimizely.", b].join("");
        d.insertBefore(c, d.firstChild)
    }

    function Tb(a, b, c, d) {
        a = [a, "=", encodeURIComponent(b), "; domain=.", c, "; path=/"];
        d && a.push("; expires=", (new Date(+new Date + 1E3 * d)).toUTCString());
        document.cookie = a.join("")
    }

    var Qb = "", Pb = "", T = "", Rb = [];
    var U;

    function Va() {
        function a() {
            return U.A
        }

        U = U || lb();
        Va = a;
        return a()
    }

    function Wb() {
        var a = "";
        try {
            a = navigator.userLanguage || window.navigator.language, a = a.toLowerCase()
        } catch (b) {
            a = ""
        }
        return a
    }

    function Db() {
        function a() {
            return U.B
        }

        U = U || lb();
        Db = a;
        return a()
    }

    function Xb(a) {
        if (!a)return "";
        try {
            return a.match(/:\/\/(?:www[0-9]?\.)?(.[^/:]+)/)[1]
        } catch (b) {
            return ""
        }
    }

    function Yb() {
        return y("optimizelyReferrer") || document.referrer || ""
    }

    function Cb() {
        var a = y("optimizelyEndUserId");
        a || (a = "oeu" + +new Date + "r" + Math.random(), z("optimizelyEndUserId", a, 31536E4));
        return a
    }

    function Wa() {
        var a = {};
        try {
            a = window.optimizely.data.visitor.location
        } catch (b) {
        }
        var c = "", d = "", e = "", f = "";
        try {
            d = a.country.toUpperCase() || ""
        } catch (g) {
            d = ""
        }
        try {
            e = a.region.toUpperCase() || ""
        } catch (l) {
            e = ""
        }
        "N/A" === e && (e = "");
        try {
            f = a.city.toUpperCase() || ""
        } catch (t) {
            f = ""
        }
        "N/A" === f && (f = "");
        try {
            c = a.continent.toUpperCase() || ""
        } catch (n) {
            c = ""
        }
        "N/A" === c && (c = "");
        return {city: f, continent: c, country: d, region: e}
    }

    function Za() {
        function a() {
            return U.L
        }

        U = U || lb();
        Za = a;
        return a()
    }

    function $a() {
        var a = navigator.appVersion || "", b = "";
        -1 !== a.indexOf("Win") && (b = "Windows");
        -1 !== a.indexOf("Mac") && (b = "Mac");
        -1 !== a.indexOf("Linux") && (b = "Linux");
        return b
    }

    function Zb() {
        var a = Yb();
        if (hb("utm_source") || hb("gclid"))return "campaign";
        for (var b = ["google\\.\\w{2,3}(\\.\\w{2,3})?/(search|url)", "https://(www)?\\.google\\..*?/$", "bing\\.\\w{2,3}(\\.\\w{2,3})?/(search|url)", "yahoo\\.\\w{2,3}(\\.\\w{2,3})?/search", "baidu\\.\\w{2,3}(\\.\\w{2,3})?/s?"], c = 0; c < b.length; c++)if (a.match(b[c]))return "search";
        return a && Xb(a) !== Xb($b || window.location.href) ? "referral" : j
    }

    var $b = h, ac = h;

    function bc(a, b) {
        if (F) {
            S(a) ? cc(a) : (a = [], cc(b));
            a = a.concat(D);
            D = [];
            for (var c = 0; c < a.length; c++)E(ua, a[c]) || ua.push(a[c]);
            c = a;
            c === h ? c = [] : vb(c) && (c = [c]);
            for (var d = va(c), e = [], f = [], g = [], l = [], t = dc(ec(), function (a) {
                return a.experiments ? k : fc(a.url_conditions || [])
            }), n = 0, w = t.length; n < w; n++)e.push({
                g: t[n].event_name,
                f: t[n].selector,
                type: "event '" + t[n].event_name + "' (click goal)",
                i: i
            });
            p(c, function (a) {
                var b = {}, c = u(a, "events") || {};
                p(c, function (a, c) {
                    b[a] = [c]
                });
                for (var c = dc(ec(), function (b) {
                    return E(b.experiments ||
                        [], a)
                }), d = 0; d < c.length; d++) {
                    var n = c[d];
                    b[n.selector] || (b[n.selector] = []);
                    b[n.selector].push(n.event_name)
                }
                p(b, function (b, c) {
                    p(c, function (c) {
                        e.push({g: c, f: b, type: "event '" + c + "' (experiment " + a + ")", i: i})
                    })
                });
                c = u(a, "css") || "";
                d = u(a, "code") || "";
                c && g.push({
                    code: '$("body").append("<style>' + c.replace(/([\f\n\r\t\\'"])/g, "\\$1") + '</style>");',
                    f: "body",
                    type: "global css (experiment " + a + ")",
                    i: i
                });
                d && gc(d, f, l)
            });
            p(d, function (a) {
                for (var a = za(a), a = a.split("\n"), b = [], c = i, d = 0, e = a.length; d < e; d++) {
                    var g = $.trim(a[d]);
                    if (g === "/* _optimizely_variation_url_end */")c = i; else if (g !== "") {
                        var n = Ob.exec(g);
                        if (n && n.length === 13) {
                            var w = n[2] ? n[2].split(" ") : [], g = n[4] ? n[4].split(" ") : [], t = n[6] ? n[6] : "substring", od = n[8] ? n[8].split(" ") : [], n = n[10] ? n[10].split(" ") : [];
                            if (w.length > 0) {
                                c = hc(w, od, t);
                                c = fc(c)
                            }
                            if (c && g.length > 0) {
                                c = hc(g, n, t);
                                c = !fc(c)
                            }
                        } else c && b.push(g)
                    }
                }
                a = b.join("\n");
                gc(a, f, l)
            });
            c = [];
            c.push.apply(c, f);
            c.push.apply(c, g);
            c.push.apply(c, l);
            c.push.apply(c, e);
            ic.push.apply(ic, c);
            jc()
        }
    }

    function jc() {
        var a = k;
        kc = j;
        for (v("Evaluator", lc + " times waited"); !a && 0 < ic.length;) {
            v("Evaluator", ic.length + " steps remaining");
            var b = ic.shift(), c = b, a = k;
            if (c.T && !mc)v("Evaluator", "Document not ready yet"), a = i; else if (c.i && !mc && (c = c.f))for (var c = S(c) ? c : [c], d = 0; d < c.length; d++) {
                var e = c[d];
                if (!(e === j || e === h || !e.length) && 0 === $(e).length)v("Evaluator", "'" + e + "' not found"), a = i
            }
            a ? ic.unshift(b) : b.g ? (v("Evaluator", "Bound event " + b.g + " to selector " + b.f), nc(b.f, b.g)) : b.code && (v("Evaluator", "Run code: " + b.code),
                oc(b.code))
        }
        a ? (kc = setTimeout(jc, 0 === lc ? 10 : 50), lc++) : v("Evaluator", lc + " total times waited")
    }

    function oc(a) {
        a = a.replace(Gb, Eb);
        if (ya(a))if (v("Evaluator", "Redirect detected"), kb(a))v("Evaluator", "OK to redirect"), jb(a) || (v("Evaluator", "NOT setting a redirect cookie"), z("optimizelyRedirect", window.location.href, 5)), z("optimizelyReferrer", document.referrer, 5); else {
            v("Evaluator", "NOT OK to redirect");
            return
        }
        eval("var $j = $;");
        try {
            eval(a)
        } catch (b) {
            var c = R;
            R = i;
            v("Evaluator", "Error: " + b.message);
            v("Evaluator", "Code: " + a);
            R = c;
            v("Evaluator", "Failed to run code: " + b.message)
        }
    }

    function nc(a, b) {
        if (!pc[a] || !pc[a][b]) {
            var c = "mousedown", d = Za();
            if ("iphone" === d || "ipad" === d || "ipod" === d)c = "touchstart";
            $(a).bind(c, function () {
                G.e.call(G, b, "custom")
            });
            pc[a] || (pc[a] = {});
            pc[a][b] = c
        }
    }

    function cc(a) {
        a || (a = Qa());
        for (var b = 0; b < a.length; b++) {
            var c = a[b], d = O[c];
            d ? (wb("Not activating " + A(c) + " because " + d + ".", {type: "explanation"}), delete O[c]) : wb("Activating " + A(c) + ".", {
                type: "activation",
                experimentId: c
            })
        }
    }

    var pc = {}, ua = [], D = D || [], qc = 0, mc = k, ic = [], kc = j, lc = 0;
    $(function () {
        mc = i;
        kc !== j && (v("Evaluator", "Document is ready"), clearTimeout(kc), 0 < qc ? setTimeout(jc, qc) : jc())
    });
    function Ab() {
        return C("admin_account_id")
    }

    function ec() {
        if (!rc) {
            var a = C("click_goals") || [];
            rc = [];
            for (var b = 0, c = a.length; b < c; b++)for (var d = a[b], e = d.selector.split(","), f = 0, g = e.length; f < g; f++) {
                var l = e[f];
                l && (l = {
                    event_name: d.event_name,
                    selector: l
                }, d.experiments !== h ? l.experiments = d.experiments : d.url_conditions !== h && (l.url_conditions = d.url_conditions), rc.push(l))
            }
        }
        return rc
    }

    function Bb() {
        var a = dc(Qa(), wa);
        Bb = function () {
            return a
        };
        return a
    }

    function sc(a) {
        var b = C("segments") || {}, c;
        for (c in b)if (Object.prototype.hasOwnProperty.call(b, c)) {
            var d = b[c];
            if (d && d.api_name === a)return String(c)
        }
        return j
    }

    function bb() {
        return C("experiments") || {}
    }

    function Qa() {
        return Ua(C("experiments") || {})
    }

    function Ra(a) {
        return u(a, "manual") || k
    }

    function A(a) {
        return 'experiment "' + (u(a, "name") || "") + '" (' + a + ")"
    }

    function K(a) {
        return u(a, "section_ids") || []
    }

    function Fa(a) {
        return u(a, "variation_ids") || []
    }

    function zb() {
        return C("project_id")
    }

    function Sa() {
        return Ua(C("segments") || {})
    }

    function tc(a) {
        var b = {}, c = C("public_suffixes") || {};
        p(c, function (a, c) {
            p(c, function (c) {
                b[c] = a
            })
        });
        tc = function (a) {
            return b[a] || ""
        };
        return tc.call(j, a)
    }

    function Ga(a) {
        return C("sections", a, "variation_ids") || []
    }

    function za(a) {
        var b = [];
        p(a.split("_"), function (a) {
            (a = C("variations", a, "code")) && b.push(a)
        });
        return b.join("\n")
    }

    function q(a) {
        var b = {};
        p(Qa(), function (a) {
            p(K(a), function (d) {
                p(Ga(d), function (d) {
                    b[d] = a
                })
            });
            p(Fa(a), function (d) {
                b[d] = a
            })
        });
        q = function (a) {
            return b[a.split("_")[0]] || ""
        };
        return q.call(j, a)
    }

    function oa(a) {
        var b = q(a), c = K(b);
        if (0 === c.length) {
            c = Fa(b);
            for (b = 0; b < c.length; b++)if (c[b] === a)return b
        } else {
            for (var a = a.split("_"), b = [], d = 0; d < c.length; d++)for (var e = Ga(c[d]), f = 0; f < e.length; f++)e[f] === a[d] && b.push(f);
            if (b !== [])return b
        }
        return -1
    }

    function xa(a) {
        var b;
        return Aa(a).join(b || ", ")
    }

    function Aa(a) {
        var b = [];
        p(a.split("_"), function (a) {
            b.push(C("variations", a, "name") || "Unnamed")
        });
        return b
    }

    function wa(a) {
        return !!u(a, "enabled")
    }

    function u(a, b) {
        return C("experiments", a, b)
    }

    function qa(a) {
        return u(a, "google_analytics")
    }

    function C(a) {
        var b = DATA;
        if (p(arguments, function (a) {
                a = b[a];
                if (B(a))b = a; else return j
            }) !== j)return b
    }

    function Ta(a, b) {
        return C("segments", a, b)
    }

    function yb(a) {
        var a = C(a), b = document.location.protocol;
        "chrome-extension:" === b && (b = "http:");
        return b + "//" + a
    }

    var rc = j;

    function uc(a) {
        var b = k;
        if (u(a, "uses_geotargeting") || Ta(a, "uses_geotargeting"))try {
            var c = window.optimizely.data.visitor.location;
            if (0 < Ua(c) && ("" !== c.continent || "" !== c.country || "" !== c.region || "" !== c.city))b = i
        } catch (d) {
        } else b = i;
        b || v("Condition", "Not ready to test (geotargeting): " + a);
        return b
    }

    function vc(a, b) {
        var c = b.manualMode === i, d = b.objectType ? b.objectType : "experiment", e = "experiment" === d, f = b.defaultFail === i;
        v("Condition", "Testing " + d + " " + a);
        var g = e && wa(a), l = e && Ra(a), t;
        a:switch (d) {
            case "experiment":
                t = u(a, "conditions") || [];
                break a;
            case "segment":
                t = Ta(a, "add_condition") || [];
                break a;
            default:
                t = []
        }
        if (e && !g && pb !== a)return v("Condition", "Failed for " + d + " " + a + " (paused)"), O[a] = "it is paused", k;
        if (e && !c && l)return v("Condition", " Failed for " + d + " " + a + " (manual activation mode)"), O[a] = "it is set to use manual activation mode",
            k;
        var n = "experiment" === (d || "experiment"), w = i;
        f && (w = k);
        p(t, function (b) {
            var c = b.type;
            if (n && b.only_first_time && wc(a))v("Condition", c + " condition passed because it only gets checked when bucketing", i); else {
                var d = !b.not, e = (0, xc[c])(b), b = e !== d, c = "the visitor " + (e ? "passed" : "failed") + " a " + c + " targeting condition  when it needed to " + (d ? "pass" : "fail");
                v("Condition", "Found that " + c, !b);
                if (b)return w = k, O[a] = c, k
            }
        });
        return !w ? (v("Condition", "Failed for " + d + " " + a + " (condition failed)"), k) : i
    }

    function fc(a) {
        for (var b = $b || window.location.href, c = 0; c < a.values.length; c++) {
            var d = a.values[c], e = d.value, d = d.match, f = aa(b, e, d);
            v("Condition", "Testing URL " + b + " against  " + e + " (" + d + ")", i);
            if (f)return i
        }
        return k
    }

    var xc = {
        browser: function (a) {
            var b = Va(), c = Db(), d = k, e = Za();
            p(a.values, function (a) {
                "unknown" !== e ? (d = "mobile" === a || a === e, v("Condition", e, i)) : 0 === a.indexOf(b) && (a = a.substr(b.length), d = "" === a || a <= c && c < Number(a) + 1);
                if (d)return j
            });
            return d
        }, code: function (a) {
            a = a.value;
            if (a === h)return i;
            try {
                return Boolean(eval(a))
            } catch (b) {
                return k
            }
        }, cookies: function (a) {
            for (var b = k, c = a.names || [], a = a.values || [], d = 0; d < c.length; d++) {
                var e = y(c[d]);
                if (b = B(a[d]) && "" !== yc(a[d]) ? b || a[d] === e : b || e !== j && e !== h)return i
            }
            return k
        }, event: function (a) {
            var b =
                y("optimizelyCustomEvents") || "{}";
            try {
                b = ub(b)
            } catch (c) {
                b = {}
            }
            var d = b[Cb()] || [];
            S(d) || (d = []);
            var e = k;
            p(a.values, function (a) {
                if (-1 !== $.inArray(a, d))return e = i
            });
            return e
        }, language: function (a) {
            var b = Wb(), c = k;
            p(a.values, function (a) {
                if (c = "any" === a || 0 === b.indexOf(a))return j
            });
            return c
        }, location: function (a) {
            for (var b = Wa(), c = 0; c < a.values.length; c++) {
                var d = a.values[c].split("|"), e = $.trim(d[0]), f = $.trim(d[1]), g = $.trim(d[2]), l = $.trim(d[3]);
                switch (d.length) {
                    case 1:
                        if (b.country === e)return i;
                        break;
                    case 2:
                        if (b.region ===
                            f && b.country === e)return i;
                        break;
                    case 3:
                        if (b.city === g && (b.region === f || "" === f) && b.country === e)return i;
                        break;
                    case 4:
                        if (b.continent === l)return i
                }
            }
            return k
        }, query: function (a) {
            if (0 === a.values.length)return i;
            var b = k, c = Xa();
            p(a.values, function (a) {
                for (var e = a.key, a = a.value || "", f = 0; f < c.length; f++) {
                    var g = c[f], l = g[0], g = g[1];
                    if ("" !== e && e === l && ("" === a || a === g))return b = i
                }
            });
            return b
        }, referrer: function (a) {
            for (var b = document.referrer, c = 0; c < a.values.length; c++) {
                var d = a.values[c], e = d.value, d = d.match, f = aa(b, e, d);
                v("Condition",
                    "Testing referrer " + b + " against  " + e + " (" + d + ")", i);
                if (f)return i
            }
            return k
        }, segment: function (a) {
            var b = k, c = [];
            p(V, function (a, b) {
                b && c.push(a)
            });
            p(a.values, function (a) {
                E(c, a) && (b = i)
            });
            return b
        }, url: fc, visitor: function (a) {
            var b = ac ? "returning" : "new";
            switch (a.value) {
                case "new":
                    if ("returning" === b)return k;
                    break;
                case "returning":
                    return "returning" === b
            }
            return i
        }
    };

    function zc() {
        v("Segmenter", "Loading segments cookie.");
        var a = y("optimizelySegments");
        if (a) {
            try {
                a = ub(a)
            } catch (b) {
                a = {}
            }
            p(a, function (a, b) {
                v("Segmenter", "Segments cookie contains segment id: " + a);
                V[a] = b
            })
        }
    }

    function Ac() {
        var a = {};
        p(V, function (b, c) {
            c && (a[b] = c)
        });
        z("optimizelySegments", tb(a), 31536E4)
    }

    function Bc(a, b) {
        var c = sc(a) || a;
        c && !isNaN(parseInt(c, 10)) ? (!b && "" !== b && (b = i), V[c] = b, Cc()) : v("Segmenter", "Unable to find segment for ID: " + c)
    }

    function Cc() {
        p(Dc, function (a) {
            a()
        })
    }

    function Ec(a) {
        v("Segmenter", "Evaluating Segment " + a);
        var b = vc(a, {objectType: "segment"}), c = O[a];
        c ? (wb("Not adding visitor to segment " + (Ta(a, "name") || "") + " because " + c + ".", {type: "explanation"}), delete O[a]) : wb("Segment add condition matches, addingto segment " + (Ta(a, "name") || ""), {type: "explanation"});
        if (b) {
            var d;
            a:{
                c = b = j;
                switch (Ta(a, "segment_value_type") || "") {
                    case "browser":
                        b = Va();
                        c = "unknown";
                        break;
                    case "campaign":
                        b = hb("utm_campaign");
                        c = "none";
                        break;
                    case "country":
                        b = Wa().country;
                        c = "unknown";
                        break;
                    case "language":
                        b = Wb();
                        c = "none";
                        break;
                    case "mobile":
                        b = "unknown" !== Za();
                        break;
                    case "os":
                        b = $a();
                        c = "unknown";
                        break;
                    case "referrer":
                        b = Yb();
                        b = Xb(b);
                        c = "none";
                        break;
                    case "source_type":
                        b = Zb();
                        c = "direct";
                        break;
                    default:
                        d = "true";
                        break a
                }
                if (b === j) {
                    if (V.hasOwnProperty(a)) {
                        d = j;
                        break a
                    }
                    b = c
                }
                d = d || Fc;
                d = String(b).toLowerCase().substring(0, d)
            }
            d !== j && Bc(a, d)
        }
    }

    function Gc() {
        v("Segmenter", "Evaluating all segments.");
        for (var a = Sa(), b = 0; b < a.length; b++) {
            var c = a[b];
            Ta(c, "is_api_only") ? v("Segmenter", "Not doing anything since segment " + c + " is api only.") : (v("Segmenter", "Testing whether to add to segment " + c), uc(c) ? Ec(c) : db.push(c))
        }
    }

    function Ya() {
        var a = {};
        p(V, function (b, c) {
            c && (a[String(b)] = c)
        });
        return a
    }

    function Hc(a, b) {
        var c = sc(a) || a;
        V[c] ? (V[c] = k, ("undefined" === typeof b || b) && Cc()) : v("Segmenter", "Not removing " + a + ", not found")
    }

    var Dc = [], V = {}, Fc = 20;

    function v(a, b, c) {
        Ic.push({D: new Date, C: a, message: b, n: c || k});
        Jc && Kc()
    }

    function Kc() {
        R && (p(Ic, function (a) {
            if (!a.J && (!a.n || a.n === rb)) {
                var b = +a.D;
                o(a.C, a.message + (" [time " + (Lc ? b - Lc : 0) + " +" + (Mc ? b - Mc : 0) + "]"));
                Mc = b;
                Lc || (Lc = b);
                a.J = i
            }
        }), Jc = i)
    }

    var Mc = j, Lc = j, Ic = [], Jc = k;

    function E(a, b) {
        for (var c = 0; c < a.length; c++)if (b == a[c])return i;
        return k
    }

    function gb(a) {
        var b = a.length;
        if (0 === b)return j;
        if (1 === b)return 0;
        for (var c = 0, d = 0; d < b; d++)c += a[d];
        c *= Math.random();
        for (d = 0; d < b; d++) {
            if (c < a[d])return d;
            c -= a[d]
        }
        return Math.floor(Math.random() * b)
    }

    function xb(a, b) {
        var c = Nc(arguments, 1);
        return function () {
            var b = Nc(arguments);
            b.unshift.apply(b, c);
            return a.apply(this, b)
        }
    }

    function p(a, b) {
        var c = j;
        if (S(a))for (var d = a.length, e = 0; e < d && !(c = b.call(h, a[e], e), B(c)); ++e); else for (d in a)if (Object.prototype.hasOwnProperty.call(a, d) && (c = b.call(h, d, a[d]), B(c)))break;
        return c
    }

    function ab(a, b) {
        p(b, function (b, d) {
            a[b] = d
        })
    }

    function dc(a, b) {
        for (var c = [], d = 0, e = a.length; d < e; d++) {
            var f = a[d];
            b(f) && c.push(f)
        }
        return c
    }

    function Oc(a, b) {
        return p(b, function (b) {
                if (b === a)return i
            }) || k
    }

    function S(a) {
        return a && "object" === typeof a && a.length && "number" === typeof a.length
    }

    function B(a) {
        return "undefined" !== typeof a
    }

    function vb(a) {
        return ("number" === typeof a || "string" === typeof a) && Number(a) == a
    }

    function Ua(a) {
        Ua = Object.U || function (a) {
                var c = [];
                p(a, function (a) {
                    c.push(a)
                });
                return c
            };
        return Ua.call(j, a)
    }

    function Jb(a) {
        var b = document.head || document.getElementsByTagName("head")[0] || document.documentElement, c = document.createElement("script");
        c.src = a;
        c.type = "text/javascript";
        b.appendChild(c)
    }

    function o(a, b, c) {
        var d = window.console;
        if (R && d && d.log) {
            var e = Nc(arguments, 1);
            e[0] = "Optimizely / " + a + " / " + b;
            Function.prototype.apply.call(d.log, d, e)
        }
    }

    function Nc(a, b) {
        return Array.prototype.slice.call(a, b || 0, a.length)
    }

    function yc(a) {
        return a.replace(/^[\s\xa0]+|[\s\xa0]+$/g, "")
    };
    function Rc(a) {
        return function (b) {
            if ("object" === typeof b && Sc()) {
                var c = j, d;
                for (d in b)b.hasOwnProperty(d) && (c = a.call(this, d, b[d]));
                return c
            }
            return a.apply(this, arguments)
        }
    }

    function Sc() {
        for (var a in{})return i;
        return k
    };
    function Tc() {
        for (var a = P, b = 0, c = Uc.length; b < c; b++) {
            var d = Uc[b];
            if (d.test(a))return k
        }
        b = 0;
        for (c = Vc.length; b < c; b++)if (d = Vc[b], d.test(a))return i;
        return k
    }

    var Uc = [/(edit|preview)(-local)?\.optimizely\.com/, /optimizelyedit\.appspot\.com/], Vc = [/^(https:)?\/\/www\.local\//, /^(https:)?\/\/([A-Za-z0-9\-]+\.)?optimizely\.(com|test)\//, /^(https:)?\/\/([A-Za-z0-9\-]+\.)?optimizely(-hrd)?\.appspot\.com\//];

    function Ja(a, b, c, d) {
        var c = c === i, d = d === i, e = k, f = q(a);
        if (f && (d || !wc(f))) {
            e = i;
            if (d && wc(f))for (d = 0; d < I.length; d++)I[d].j === f && I.splice(d, 1);
            I.push({j: f, id: a, source: b});
            c && (D = D || [], D.push(f));
            Da[f] = i;
            Ea();
            v("Plan", "Added experiment " + f + " and variation id " + a + " to the plan, source is " + b, i)
        }
        return e
    }

    function wc(a) {
        return a in H || a in Da
    }

    function va(a) {
        var b = [], c = !B(a), a = a || [];
        p(I, function (d) {
            (c || E(a, d.j)) && b.push(d.id)
        });
        return b
    }

    function fb(a) {
        var b;
        if (b === i || !wc(a))H[a] = i, Ea()
    }

    function Ka() {
        var a = {};
        p(Wc, function (b, c) {
            a[b] = c
        });
        p(I, function (b) {
            var c = q(b.id);
            a[c] = b.id
        });
        p(H, function (b) {
            a[b] = "0"
        });
        z("optimizelyBuckets", tb(a), 31536E4)
    }

    function Ea() {
        p(Xc, function (a) {
            a()
        })
    }

    function gc(a, b, c) {
        if (-1 !== a.indexOf("_optimizely_redirect"))b.push({code: a, type: "code forced (redirect)"}); else {
            for (var a = a.split("\n"), d = k, e = k, f = [], g = []; 0 < a.length;) {
                var l = yc(a.shift()), t = 0 < g.length;
                if (l)if (Boolean(l.match(/_optimizely_evaluate\s{0,9}=\s{0,9}force/i)))e = i; else if (Boolean(l.match(/_optimizely_evaluate\s{0,9}=\s{0,9}safe/i)) || Boolean(l.match(/_optimizely_evaluate\s{0,9}=\s{0,9}end_force/i)))e = k; else if (Boolean(l.match(/_optimizely_evaluate\s{0,9}=\s{0,9}editor_only/i)))d = i; else if (Boolean(l.match(/_optimizely_evaluate\s{0,9}=\s{0,9}end_editor_only/i)))d =
                    k; else if (!d)if (e)f.push(l); else {
                    if (!t) {
                        var n = Yc.exec(l), w = [];
                        n ? (w.push(n[1]), (n = Zc.exec(l)) && 4 < n.length && w.push(n[4]), c.push({
                            code: l,
                            f: w,
                            type: "safe jquery",
                            i: i
                        })) : t = i
                    }
                    t && g.push(l)
                }
            }
            0 < f.length && b.push({code: f.join("\n"), type: "forced evaluation"});
            0 < g.length && c.push({code: g.join("\n"), type: "safe non-jquery", T: i})
        }
    }

    function hc(a, b, c) {
        for (var d = {values: []}, e = 0, f = a.length; e < f; e++)d.values.push({value: a[e], match: b[e] || c});
        return d
    }

    var Xc = [], Wc = {}, H = {}, Yc = /^\$j?\(['"](.+?)['"]\)\..+;\s*$/, Zc = /^\$j?\(['"](.+?)['"]\)\.detach\(\)\.(appendTo|insertAfter|insertBefore|prependTo)\(['"](.+?)['"]\);\s*$/, Da = {}, I = [];
    if (!Mb) {
        var X = $;
        X.fn.attr = Rc(X.fn.attr);
        X.fn.css = Rc(X.fn.css);
        X.fn.extend = Rc(X.fn.extend);
        var $c = X.each;
        X.each = function (a, b, c) {
            if (!(a.length === h || X.isFunction(a)) || !Sc())$c.apply(this, arguments); else if (c)for (var d in a) {
                if (a.hasOwnProperty(d) && b.apply(a[d], c) === k)break
            } else for (d in a)if (a.hasOwnProperty(d) && !b.call(a[d], d, a[d]) === k)break;
            return a
        };
        var ad = X.fn.H, bd = function (a, b, c) {
            return new ad(a, b, c)
        }, cd, dd = document.getElementsByClassName;
        if (!ib(dd))var ed = (window.optimizely || {}).getElementsByClassName,
            fd = (window.optly || {}).getElementsByClassName, dd = ib(ed) ? ed : ib(fd) ? fd : j;
        cd = dd;
        X.fn.H = function (a, b, c) {
            var d = bd, e = document.getElementsByClassName;
            !ib(e) && cd && (d = function (a, b, c) {
                document.getElementsByClassName = cd;
                a = bd(a, b, c);
                document.getElementsByClassName = e;
                return a
            });
            if (!("string" === typeof a && b && "object" === X.type(b) && Sc()))return d(a, b, c);
            a = d(a, h, c);
            a.attr(b);
            return a
        }
    }
    v("Main", "Started, revision " + C("revision"));
    for (var gd = window.location.search, hd, id = /optimizely_([^=]+)=([^&]*)/g, Y = {}; hd = id.exec(gd);)Y[hd[1]] = decodeURIComponent(hd[2]);
    var jd = /x(\d+)/, kd = k;
    p(Y, function (a, b) {
        var c = jd.exec(a);
        if (c && (kd = i, pb = c = c[1], "-1" !== b)) {
            var d, e = b.split("_");
            d = K(c);
            var f = [];
            if (d.length === e.length)p(d, function (a, b) {
                var c = e[b];
                if (c = Ga(a)[c])f.push(c); else return f = [], j
            }); else if (1 === e.length) {
                d = Fa(c);
                var g = e[0], l = d[g];
                !l && E(d, g) && (l = g);
                l && f.push(l)
            }
            d = f.join("_");
            g = !Ra(c);
            Ja(d, "query", g);
            Lb.push(c)
        }
    });
    ("true" === Y.opt_out || "false" === Y.opt_out) && G.q("true" === Y.opt_out);
    var F = "true" !== Y.disable && "true" !== Y.opt_out && "true" !== y("optimizelyOptOut"), P = Y.load_script, qb = "true" === Y.preview, R = "true" === Y.log, rb = "true" === Y.verbose, ld = "true" === Y.force_tracking, kd = kd || qb, m = !kd || ld;
    "false" === Y.client && (F = k, P = "js/" + zb() + ".js");
    P && (Tc() || (P = ""));
    var md = document.location.hostname, Z = md.split("."), nd = md, pd = Z[Z.length - 1];
    2 < Z.length && "appspot" === Z[Z.length - 2] && "com" === pd ? nd = Z[Z.length - 3] + ".appspot.com" : 1 < Z.length && Oc(pd, Nb) && (nd = Z[Z.length - 2] + "." + pd);
    Qb = nd;
    o("Cookie", "Guessed public suffix: %s", Qb);
    T = tc(md);
    o("Cookie", "Public suffix (from data): %s", T);
    Pb && o("Cookie", "Api public suffix (from api): %s", Pb);
    T || (T = y("optimizelyPublicSuffix") || "", o("Cookie", "Public suffix (from cookie): %s", T));
    !Pb && (!T && C("remote_public_suffix")) && (o("Cookie", "Making request for public suffix on DOM ready"), $(xb(Vb, md)));
    var qd = y("optimizelyBuckets"), ac = qd !== h && qd !== j, rd;
    a:{
        for (var sd = ["googlebot", "yahoo! slurp", "bingbot", "msnbot"], td = navigator.userAgent.toLowerCase(), ud = 0; ud < sd.length; ud++)if (-1 !== td.indexOf(sd[ud])) {
            rd = i;
            break a
        }
        rd = k
    }
    rd && (m = k);
    var vd = y("optimizelyBuckets");
    if (vd) {
        try {
            vd = ub(vd)
        } catch (wd) {
            vd = {}
        }
        var xd = {};
        p(vd, function (a, b) {
            var b = String(b), c = q(b);
            if (K(c).length > 1 && b.indexOf("_") === -1) {
                xd[c] = xd[c] || {};
                xd[c][a] = b
            } else b !== "0" ? Ja(b, "cookie", k) || (Wc[a] = b) : fb(a)
        });
        p(xd, function (a, b) {
            var c;
            a:{
                c = [];
                for (var d = K(a), e = 0; e < d.length; e++) {
                    var f = b[d[e]];
                    if (f === "0") {
                        c = "";
                        break a
                    }
                    c.push(f)
                }
                c = c.join("_")
            }
            c.length > 0 ? Ja(c, "cookie", k) : fb(a)
        })
    }
    zc();
    Gc();
    Dc.push(Ac);
    Cc();
    Dc.push(Na);
    Xc.push(Na);
    var yd = {
        $: $,
        activeExperiments: ua || [],
        allExperiments: bb(),
        all_experiments: bb(),
        allVariations: C("variations") || {},
        data: N,
        getElementsByClassName: document.getElementsByClassName,
        revision: C("revision"),
        variationIdsMap: Oa,
        variation_map: M,
        variationMap: M,
        variationNamesMap: Pa
    }, zd = {}, Ad = xb(function (a, b) {
            var c = [], d = b;
            if (S(b)) {
                c = Nc(b, 1);
                d = b[0]
            }
            var e = a[d];
            if (e) {
                v("API", 'Called function "' + d + '"');
                d !== "acknowledgePreviewMode" && wb(d, {type: "api"});
                e.apply(j, c)
            } else v("API", 'Error for unknown function "' + d + '"');
            Kc()
        },
        zd);
    ab(zd, {
        acknowledgePreviewMode: function () {
            v("Preview", "Preview acknowledgement received");
            Kb = i
        }, activate: function (a, b, c) {
            if (!F)return k;
            var d = typeof b === "number" || typeof b === "string" ? String(b) : j, e = b === i || b && b.force === i || c && c.force === i, f = typeof b === "object" && b.skip === i || typeof c === "object" && c.skip === i, b = typeof b === "object" && b.skipPageview === i || typeof c === "object" && c.skipPageview === i;
            if (d)try {
                Ba(a, d, i)
            } catch (g) {
                v("API", "Error while activating experiment " + a + " for variation " + d + " -- proceeding without bucketing user.")
            }
            var l = j,
                t = [], n = [];
            vb(a) ? t.push(a) : p(Qa(), function (a) {
                Ra(a) && t.push(a)
            });
            v("API", "Testing experiments to activate");
            p(t, function (a) {
                if (!e && !uc(a))cb.push(a); else if (e || vc(a, {
                        manualMode: i,
                        objectType: "experiment"
                    }))(l = eb(a, f)) && n.push(a)
            });
            bc(n, t);
            Ka();
            ma();
            F && !b && G.e(document.location.href)
        }, activateGeoDelayedExperiments: function () {
            if (!F)return k;
            v("API", "Testing geodelayed segments");
            p(db, function (a) {
                Ec(a)
            });
            var a = j, b = cb, c = [];
            v("API", "Testing geodelayed experiments");
            p(b, function (b) {
                if (vc(b, {
                        manualMode: i,
                        objectType: "experiment"
                    }))(a = eb(b)) && c.push(b)
            });
            bc(c, b);
            Ka();
            ma();
            F && !sb && G.e(document.location.href);
            cb = [];
            db = [];
            window.optimizelyGeo = {};
            return i
        }, activateSiteCatalyst: ga, addToSegment: Bc, bindTrackElement: function (a, b) {
            if (a && b)if (mc) {
                v("Evaluator", "Bound event " + b + " to selector " + a);
                nc(a, b)
            } else {
                var c = {g: b, f: a, type: "event '" + b + "' (click goal)", i: i};
                v("Evaluator", "Add step to bind event " + c.g + " to selector " + c.f);
                ic.push(c)
            }
        }, bucketUser: Ba, bucketVisitor: Ba, clickTaleRecord: function () {
            o("Integrator",
                "Tracking with ClickTale.");
            typeof window.ClickTaleField == "function" ? p(ka(), function (a) {
                var b = q(a), c = r(b, a, 100, 100, 15, k), c = c.key + ": " + c.value + " (x" + b + "=" + a + ")";
                o("Integrator", "Setting ClickTale - %s", c);
                window.ClickTaleField(b, a);
                window.ClickTaleEvent(c)
            }) : o("Integrator", "ClickTaleField() not defined.")
        }, clickTalePlayback: function () {
            if (window.ClickTaleContext) {
                try {
                    window.ClickTaleContext.getAggregationContextAsync("1", function (a) {
                        a.Location && window.optimizely.push(["overrideUrl", a.Location]);
                        for (var b in a.PageEvents) {
                            var e =
                                a.PageEvents[b][2].match(/x[0-9]+=[0-9_]+/g);
                            o("Integrator", "Playback ClickTale Integration - %s", e);
                            for (b = 0; b < e.length; b++) {
                                o("Integrator", "Playback ClickTale Integration - %s", e[b]);
                                for (var f = e[b].split("=")[0].substr(1), g = e[b].split("=")[1].split("_"), l = 0; l < g.length; l++)ya(za(g[l])) ? o("Integrator", "Skip activation for redirect.") : window.optimizely.push(["activate", f, g[l], {force: i}])
                            }
                        }
                    })
                } catch (a) {
                    o("Integrator", "Playback ClickTale Aggregation Integration failed.")
                }
                try {
                    window.ClickTaleContext.getRecordingContextAsync("1.1",
                        function (a) {
                            if (a.inSingleRecordingScope) {
                                a.location && window.optimizely.push(["overrideUrl", a.location]);
                                o("Integrator", "Playback ClickTale getRecordingContextAsync callback");
                                for (var b in a.fields) {
                                    o("Integrator", "Playback ClickTale Integration - %s=%s", b, a.fields[b]);
                                    ya(za(a.fields[b])) ? o("Integrator", "Skip activation for redirect.") : window.optimizely.push(["activate", b, a.fields[b], {force: i}])
                                }
                            }
                        })
                } catch (b) {
                    o("Integrator", "Playback ClickTale Recording Integration failed.")
                }
            } else o("Integrator", "ClickTaleContext not defined.")
        },
        delayDomReadyEval: function (a) {
            qc = a
        }, delayPageviewTracking: function (a) {
            if (!vb(a))return k;
            ob = Number(a)
        }, disable: La, log: function () {
            R = i
        }, getAccountId: Ab, getProjectId: zb, googleAnalyticsCustomVariableScope: function (a) {
            a = vb(a) ? Number(a) : -1;
            if ([1, 2, 3].indexOf(a) !== -1)sa = a; else return sa
        }, integrationPrefix: function (a) {
            na = a
        }, optOut: G.q, overrideUrl: function (a) {
            o("User", "Setting current URL to %s", a);
            $b = a
        }, preview: function () {
            Kb || Ib()
        }, push: Ad, removeFromAllSegments: function () {
            var a = Sa();
            p(a, function (a) {
                Hc(a,
                    k)
            });
            Cc()
        }, removeFromSegment: Hc, sc_activate: ga, sc_svar: function (a) {
            ha = a
        }, setCookieDomain: function (a) {
            Pb = String(a) || "";
            o("Cookie", "Api public suffix set to: %s", Pb)
        }, skipPageTracking: function () {
            sb = i
        }, timeout: La, trackEvent: G.w, verbose: function () {
            rb = R = i
        }
    });
    ab(yd, zd);
    var Bd = window.optimizely;
    S(Bd) && p(Bd, function (a) {
        Ad(a)
    });
    window.optimizely = yd;
    window.optimizely.iapi = {
        evaluateSegments: function () {
            V = {};
            zc();
            Gc();
            Dc.push(Ac);
            Cc()
        }
    };
    v("Info", "Is enabled: " + F);
    v("Info", "Script to load: " + (P || "none"));
    v("Info", "Browser type: " + Va());
    v("Info", "Browser version: " + Db());
    var Cd = Za();
    "unknown" !== Cd && v("Info", "Mobile browser type: " + Cd);
    v("Info", "New vs returning: " + (ac ? "returning" : "new"));
    v("Info", "Source type: " + Zb());
    v("Info", "User ID: " + Cb());
    P && Jb(P);
    F && (p(Qa(), function (a) {
        if (!Oc(a, D))if (uc(a)) {
            if (vc(a, {objectType: "experiment"})) {
                v("Distributor", "Going to distribute " + A(a));
                var b = eb(a), c = k;
                if (Lb.length > 0 && !E(Lb, a)) {
                    v("Distributor", "Not going to evaluate because of preview mode, for " + A(a));
                    c = i;
                    O[a] = "it is not being previewed"
                }
                if (b && !c) {
                    D = D || [];
                    D.push(a)
                }
            }
        } else cb.push(a)
    }), Ka(), G.F(), sb || (0 < ob ? setTimeout(function () {
        G.e(document.location.href, "pageview")
    }, ob) : G.e(document.location.href, "pageview")), G.r(), ma());
    R && (p(H, function (a) {
        var b = u(a, "name") || "";
        v("Plan", "Ignore experiment '" + b + "' (" + a + ")")
    }), p(I, function (a) {
        var b = q(a.id), c = xa(a.id);
        v("Plan", A(b) + ' in variation "' + c + '" (' + a.id + ")")
    }));
    F && (bc(), v("API", "Finalizing API."), Na(), Kc(), C("installation_verified") || G.N());
    qb && Ib();
    setTimeout(function () {
        window.optimizelyCode = {}
    }, 0);
    setTimeout(function () {
        try {
            window.optimizely.iapi.geoTimedOut = !(window.optimizely.data.visitor.location.city !== "" || window.optimizely.data.visitor.location.continent !== "" || window.optimizely.data.visitor.location.country !== "" || window.optimizely.data.visitor.location.region !== "");
            window.optimizely.activateGeoDelayedExperiments = h
        } catch (a) {
        }
    }, 2E3);
    v("Main", "End of main");
    optly.Cleanse.finish();
};
optimizelyCode();
