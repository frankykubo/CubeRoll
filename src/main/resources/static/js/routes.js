 //an array, defining the routes
    export default[
        {
            //the part after '#' in the url (so-called fragment):
            hash:"welcome",
            ///id of the target html element:
            target:"router-view",
            //the function that returns content to be rendered to the target html element:
            getTemplate: welcome
        },
        {
            hash:"articles",
            target:"router-view",
            getTemplate: fetchAndDisplayArticles
        },
        {
            hash:"opinions",
            target:"router-view",
            getTemplate: createHtml4opinions
        },
        {
            hash:"addOpinion",
            target:"router-view",
            getTemplate: (targetElm) => {
                document.getElementById(targetElm).innerHTML = document.getElementById("template-addOpinion").innerHTML
                document.getElementsByClassName("active")[0].className = "";
                document.getElementById("addOpinion").className = "active";
            }
        },
        {
            hash:"contact",
            target:"router-view",
            getTemplate: (targetElm) => {
                document.getElementById(targetElm).innerHTML = document.getElementById("template-contact").innerHTML
                document.getElementsByClassName("active")[0].className = "";
                document.getElementById("contact").className = "active";
            }
        },
        {
            hash:"about",
            target:"router-view",
            getTemplate: (targetElm) => {
                document.getElementById(targetElm).innerHTML = document.getElementById("template-about").innerHTML
                document.getElementsByClassName("active")[0].className = "";
                document.getElementById("about").className = "active";
            }
        },
        {
            hash:"article",
            target:"router-view",
            getTemplate: fetchAndDisplayArticleDetail
        },
        {
            hash:"artEdit",
            target:"router-view",
            getTemplate: editArticle
        },
        {
            hash:"commSend",
            target:"router-view",
            getTemplate: addNewAComment
        },
        {
            hash:"artDelete",
            target:"router-view",
            getTemplate: deleteArticle
        },
        {
            hash:"artInsert",
            target:"router-view",
            getTemplate: (targetElm) => {
                document.getElementById(targetElm).innerHTML = document.getElementById("template-addArticle").innerHTML
                document.getElementsByClassName("active")[0].className = "";
                document.getElementById("insert").className = "active";
            }
        },
        {
            hash:"nextComm",
            target:"comment-view",
            getTemplate: slideComments
        }
    ];

    const urlBase = "https://wt.kpi.fei.tuke.sk/api";
    const articlesPerPage = 5;
    let link = [];

    function welcome(targetElm) {
        document.getElementById(targetElm).innerHTML = document.getElementById("template-welcome").innerHTML
        document.getElementsByClassName("active")[0].className = "";
        document.getElementById("welcome").className = "active";
        let signOutButton = document.querySelector('#signOutButton');
        signOutButton.addEventListener('mousedown', () => signOutButton.style.backgroundColor = 'white');
        signOutButton.addEventListener('mouseup', () => signOutButton.style.backgroundColor = null);
        if(!link.articlesMenu) {
            link.articlesMenu = `#articles/0`;
            let templateElm = document.querySelector('#template-articlesLi');
            document.getElementById("articlesLi").innerHTML = Mustache.render(
                templateElm.innerHTML,
                link
            );
        }
    }

    function createHtml4opinions(targetElm){
        document.getElementsByClassName("active")[0].className = "";
        document.getElementById("opinions").className = "active";
        let url = 'https://parseapi.back4app.com/classes/opinions';
        let opinionsVar=[];
        const init={
            headers: {
                "X-Parse-Application-Id": "8L091cvrHVEv3pXQmmddzTW6EZuaQEnEx6YxJOm7",
                "X-Parse-REST-API-Key": "NQGJO59tyMDooZaOFPQw4OGrD6KmU3aksoZzOoK0",
            },
            method: 'GET'
        };
        fetch(url,init)
            .then((response) => {
                if(response.ok){
                    return response.json();
                }else{
                    Promise.reject("FAIL");
                }
            })
            .then((responseJSON) => {
                responseJSON.results.forEach(opinion => {
                    if(opinion.isPhotographing === true){
                        opinion.isPhotographing = "Fotografujem zrkadlovkou.";
                    }else if(opinion.isPhotographing === false){
                        opinion.isPhotographing = "Nefotografujem zrkadlovkou.";
                    }
                    if(opinion.keywords) opinion.showKeywords = true;
                    opinion.createdDate = (new Date(opinion.createdAt)).toDateString();
                });
                document.getElementById(targetElm).innerHTML = Mustache.render(
                    document.getElementById("template-opinions").innerHTML,
                    responseJSON
                );
            }).catch((error) =>{
                console.log(error);
            });
    }

    function fetchAndDisplayArticles(targetElm, offsetFromHash, totalCountFromHash){

        document.getElementsByClassName("active")[0].className = "";
        document.getElementById("articles").className = "active";
        const offset=Number(offsetFromHash);
        const totalCount=Number(totalCountFromHash);
        let totalCountFromMeta;
        let offsetFromMeta;

        let urlQuery;
        if (offset && totalCount){
            urlQuery=`?tag=jacobovClanok&offset=${offset}&max=${articlesPerPage}`;
        }else{
            urlQuery=`?tag=jacobovClanok&max=${articlesPerPage}`;
        }

        const url = `${urlBase}/article${urlQuery}`;
        let articleList = [];

        fetch(url)
            .then(response =>{
                if(response.ok){
                    return response.json();
                }else{ //if we get server error
                    return Promise.reject(new Error(`Server answered with ${response.status}: ${response.statusText}.`));
                }
            })
            .then(responseJSON => {
                addArtDetailLink2ResponseJson(responseJSON);
                articleList = responseJSON.articles;
                totalCountFromMeta = Number(responseJSON.meta.totalCount);
                offsetFromMeta = Number(responseJSON.meta.offset);
                articleList.offset = offsetFromMeta+1;
                articleList.totalCount=totalCountFromMeta;
                return Promise.resolve();
            })
            .then(() => {
                let contentRequests = articleList.map(
                    oneArticle => fetch(`${urlBase}/article/${oneArticle.id}`)
                );
                return Promise.all(contentRequests);
            })
            .then(responses => {
                let failed = "";
                for (let response of responses) {
                    if (!response.ok) failed += response.url + " ";
                }
                if (failed === "") {
                    return responses; //kontrola, ci su vsetky responses ok
                } else {
                    return Promise.reject(new Error(`Failed to access the content of the articles with urls ${failed}.`));
                }
            })
            .then(responses => Promise.all(responses.map(resp => resp.json())))
            .then(articles => {
                articleList.showing = 0;
                articles.forEach((article, index) => {
                    if(article.content != null) {
                        articleList[index].content = article.content.replace(/<[^>]*>/g, "");
                        articleList[index].contentPrev = (articleList[index].content.substr(0, 130) + "...");
                    }
                    articleList.showing = offsetFromHash++;
                });
                if(offsetFromMeta > 4){
                    articleList.showPrevPage = 1;
                    articleList.prevPage=offsetFromMeta-5;
                }else{
                    articleList.showPrevPage = 0;
                }
                if(offsetFromMeta < totalCountFromMeta-5 && totalCountFromMeta > 5){
                    articleList.nextPage=offsetFromMeta+5;
                }
                articleList.showing++;
                if(articleList.showing === 1 && articleList.offset === 1){
                    delete articleList.showing;
                    delete articleList.offset;
                }
                return Promise.resolve();
            })
            .then(() => {
                document.getElementById(targetElm).innerHTML =
                    Mustache.render(
                        document.getElementById("template-articles").innerHTML,
                        articleList);
                link.articlesMenu = `#articles/${offsetFromMeta}/${totalCountFromMeta}`;
                let templateElm = document.querySelector('#template-articlesLi');
                document.getElementById("articlesLi").innerHTML = Mustache.render(
                    templateElm.innerHTML,
                    link
                );
                document.getElementById("articles").classList.add("active");
            })
            .catch (error => { ////here we process all the failed promises
                const errMsgObj = {errMessage:error};
                document.getElementById(targetElm).innerHTML =
                    Mustache.render(
                        document.getElementById("template-articles-error").innerHTML,
                        errMsgObj
                    );
            });
    }

    function addArtDetailLink2ResponseJson(responseJSON){
        responseJSON.articles =
            responseJSON.articles.map(
                article =>(
                    {
                        ...article,
                        detailLink:`#article/${article.id}/${responseJSON.meta.offset}/${responseJSON.meta.totalCount}/0`
                    }
                )
            );
    }

    function fetchAndDisplayArticleDetail(targetElm, artIdFromHash, offsetFromHash, totalCountFromHash) {
        fetchAndProcessArticle(...arguments,false);
    }

    function fetchAndProcessArticle(targetElm, artIdFromHash, offsetFromHash, totalCountFromHash, commentsOffset, commentsTotal, forEdit) {
        const url = `${urlBase}/article/${artIdFromHash}`;
        let clanok;
        fetch(url)
            .then(response =>{
                if(response.ok){
                    return response.json();
                }else{ //if we get server error
                    return Promise.reject(new Error(`Server answered with ${response.status}: ${response.statusText}.`));
                }
            })
            .then(responseJSON => {
                if(forEdit){
                    responseJSON.formTitle="Article \"" + responseJSON.title + "\" - edit";
                    responseJSON.formSubmitCall = `processArtEditFrmData(event,${artIdFromHash},${offsetFromHash},${totalCountFromHash},'${urlBase}')`;
                    responseJSON.submitBtTitle="Save article";
                    responseJSON.urlBase=urlBase;
                    responseJSON.backLink=`#article/${artIdFromHash}/${offsetFromHash}/${totalCountFromHash}`;
                    if(responseJSON.tags){
                        let idx = responseJSON.tags.indexOf("jacobovClanok");
                        if(idx > -1) responseJSON.tags.splice(idx, 1);
                    }
                    document.getElementById(targetElm).innerHTML =
                        Mustache.render(
                            document.getElementById("template-article-form").innerHTML,
                            responseJSON
                        );
                    clanok = responseJSON;
                }else{
                    responseJSON.backLink=`#articles/${offsetFromHash}/${totalCountFromHash}`;
                    responseJSON.editLink=`#artEdit/${responseJSON.id}/${offsetFromHash}/${totalCountFromHash}`;
                    responseJSON.addCommLink=`#commSend/${responseJSON.id}/${offsetFromHash}/${totalCountFromHash}`;
                    responseJSON.deleteLink=`#artDelete/${responseJSON.id}/${offsetFromHash}/${totalCountFromHash}`;
                    clanok = responseJSON;
                }
            })
            .then( () => {
                if(commentsOffset) {
                    return fetch(`${url}/comment?max=10&offset=${commentsOffset}`)
                }else{
                    return fetch(`${url}/comment?max=10&offset=0`)
                }
            })
            .then(response =>{
                if(response.ok){
                    return response.json();
                }else{ //if we get server error
                    return Promise.reject(new Error(`Server answered with ${response.status}: ${response.statusText}.`));
                }
            })
            .then((responseJSON) => {
                clanok.comments = responseJSON.comments;
                let offsetFromMeta = Number(responseJSON.meta.offset);
                let totalCountFromMeta = Number(responseJSON.meta.totalCount);
                if(offsetFromMeta > 9){
                    clanok.showPrevPage = 1;
                    clanok.prevPage=offsetFromMeta-10;
                }else{
                    clanok.showPrevPage = 0;
                }
                if(offsetFromMeta < totalCountFromMeta-10 && totalCountFromMeta > 10){
                    clanok.nextPage=`#nextComm/${artIdFromHash}/${offsetFromHash}/${totalCountFromHash}/${offsetFromMeta+10}/${totalCountFromMeta}`
                }
                if(!forEdit) {
                    document.getElementById(targetElm).innerHTML =
                        Mustache.render(
                            document.getElementById("template-article").innerHTML,
                            clanok
                        );
                }
            })
            .catch (error => { ////here we process all the failed promises
                const errMsgObj = {errMessage:error};
                document.getElementById(targetElm).innerHTML =
                    Mustache.render(
                        document.getElementById("template-articles-error").innerHTML,
                        errMsgObj
                    );
            });
    }

    function editArticle(targetElm, artIdFromHash, offsetFromHash, totalCountFromHash, commentsOffset, commentsTotal) {
        fetchAndProcessArticle(targetElm, artIdFromHash, offsetFromHash, totalCountFromHash, commentsOffset, commentsTotal,true);
    }

    function deleteArticle(targetElm, artIdFromHash, offsetFromHash, totalCountFromHash) {
        const deleteReqSettings =
        {
            method: 'DELETE'
        };
        document.getElementById(targetElm).innerHTML=`<p class="obycText">Attempting to delete article with id=${artIdFromHash}<br />... <br /> <br /></p>`;
        fetch(`${urlBase}/article/${artIdFromHash}`, deleteReqSettings)  //now we need the second parameter, an object wih settings of the request.
            .then(response => {      //fetch promise fullfilled (operation completed successfully)
                if (response.ok) {    //successful execution includes an error response from the server. So we have to check the return status of the response here.
                    window.alert("Article successfully deleted."); //no response this time, so we end here
                    window.location.hash=`#articles/${offsetFromHash}/${totalCountFromHash}`;
                } else { //if we get server error
                    return Promise.reject(new Error(`Server answered with ${response.status}: ${response.statusText}.`)); //we return a rejected promise to be catched later
                }
            })
            .catch(error => { ////here we process all the failed promises
                document.getElementById(targetElm).innerHTML+=`<p class="obycText" Attempt failed. Details: <br />  ${error}</p>`;
            });
    }

    function addNewAComment(targetElm, artIdFromHash, offsetFromHash, totalCountFromHash) {
        const newCommData = {
            text: document.getElementById("commentAdd").value.trim(),
            author: document.getElementById("commentAuthor").value.trim(),
        };

        if (!(newCommData.text && newCommData.author)) {
            window.alert("Please, enter article title and content");
            return;
        }
        const postReqSettings = //an object wih settings of the request
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8',
                },
                body: JSON.stringify(newCommData)
            };
        fetch(`${urlBase}/article/${artIdFromHash}/comment`, postReqSettings)  //now we need the second parameter, an object wih settings of the request.
            .then(response => {      //fetch promise fullfilled (operation completed successfully)
                if (response.ok) {    //successful execution includes an error response from the server. So we have to check the return status of the response here.
                    return response.json(); //we return a new promise with the response data in JSON to be processed
                } else { //if we get server error
                    return Promise.reject(new Error(`Server answered with ${response.status}: ${response.statusText}.`)); //we return a rejected promise to be catched later
                }
            })
            .then(responseJSON => { //here we process the returned response data in JSON ...
                if(offsetFromHash && totalCountFromHash) {
                    window.location.hash = `#article/${artIdFromHash}/${offsetFromHash}/${totalCountFromHash}/0`;
                }else{
                    window.location.hash = `#article/${artIdFromHash}/0/0/0`;
                }
            })
            .catch(error => { ////here we process all the failed promises
                window.alert("Error adding comment to the server!");
                window.location.hash=`#article/${artIdFromHash}/${offsetFromHash}/${totalCountFromHash}/0`;
            });
    }

    function slideComments(targetElm, artIdFromHash, offsetFromHash, totalCountFromHash, commentsOffset, commentsTotal) {
        const url = `${urlBase}/article/${artIdFromHash}`;
        fetch(`${url}/comment?max=10&offset=${commentsOffset}`)
        .then(response =>{
            if(response.ok){
                return response.json();
            }else{ //if we get server error
                return Promise.reject(new Error(`Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then((responseJSON) => {
            let komentare = [];
            komentare.comments = responseJSON.comments;
            let offsetFromMeta = Number(responseJSON.meta.offset);
            let totalCountFromMeta = Number(responseJSON.meta.totalCount);
            if(offsetFromMeta > 9){
                komentare.showPrevPage = 1;
                komentare.prevPage=`#nextComm/${artIdFromHash}/${offsetFromHash}/${totalCountFromHash}/${offsetFromMeta-10}/${totalCountFromMeta}`
            }else{
                komentare.showPrevPage = 0;
            }
            if(offsetFromMeta < totalCountFromMeta-10 && totalCountFromMeta > 10){
                komentare.nextPage=`#nextComm/${artIdFromHash}/${offsetFromHash}/${totalCountFromHash}/${offsetFromMeta+10}/${totalCountFromMeta}`;
            }
            document.getElementById(targetElm).innerHTML =
                Mustache.render(
                    document.getElementById("template-comments").innerHTML,
                    komentare
                );
        })
        .catch (error => { ////here we process all the failed promises
            const errMsgObj = {errMessage:error};
            document.getElementById(targetElm).innerHTML =
                Mustache.render(
                    document.getElementById("template-articles-error").innerHTML,
                    errMsgObj
                );
        });
    }