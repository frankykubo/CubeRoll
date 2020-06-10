function addRatingClick() {
    const sgnd = auth2.isSignedIn && auth2.isSignedIn.get();
    if (!sgnd) {
        window.alert("You have to log in if you want to add rating!");
    } else {
        document.getElementById("addRating").classList.add("hiddenElm");
        document.getElementById("addRating").classList.remove("buttons");
        document.getElementById("addRatingForm").classList.remove("hiddenElm");
        document.getElementById("closeRating").classList.remove("hiddenElm");
        updateSignIn();
    }
}

function closeRatingClick() {
    document.getElementById("addRatingForm").classList.add("hiddenElm");
    document.getElementById("closeRating").classList.add("hiddenElm");
    document.getElementById("addRating").classList.remove("hiddenElm");
    document.getElementById("addRating").classList.add("buttons");
}

function addCommentClick() {
    const sgnd = auth2.isSignedIn && auth2.isSignedIn.get();
    if (!sgnd) {
        window.alert("You have to log in if you want to add comment!");
    } else {
        document.getElementById("addComment").classList.add("hiddenElm");
        document.getElementById("addComment").classList.remove("buttons");
        document.getElementById("addCommentForm").classList.remove("hiddenElm");
        document.getElementById("closeComment").classList.remove("hiddenElm");
        updateSignIn();
    }
}

function closeCommentClick() {
    document.getElementById("addCommentForm").classList.add("hiddenElm");
    document.getElementById("closeComment").classList.add("hiddenElm");
    document.getElementById("addComment").classList.remove("hiddenElm");
    document.getElementById("addComment").classList.add("buttons");
}

function addScoreClick() {
    const sgnd = auth2.isSignedIn && auth2.isSignedIn.get();
    if (!sgnd) {
        window.alert("You have to log in if you want to submit your score!");
    } else {
        document.getElementById("addScore").classList.add("hiddenElm");
        document.getElementById("addScore").classList.remove("buttons");
        document.getElementById("addScoreForm").classList.remove("hiddenElm");
        document.getElementById("closeScore").classList.remove("hiddenElm");
        updateSignIn();
    }
}

function closeScoreClick() {
    document.getElementById("addScoreForm").classList.add("hiddenElm");
    document.getElementById("closeScore").classList.add("hiddenElm");
    document.getElementById("addScore").classList.remove("hiddenElm");
    document.getElementById("addScore").classList.add("buttons");
}

let auth2 = {};

function renderUserInfo(googleUser, htmlElmId) {
    const profile = googleUser.getBasicProfile();
    const htmlStringEn=
        `
            <p>User logged in.</p>
            <ul>
                <li> ID: ${profile.getId()}
                <li>  Full name: ${profile.getName()}
                <li>  Given name: ${profile.getGivenName()}
                <li>  Family name: ${profile.getFamilyName()}
                <li>  Image URL: ${profile.getImageUrl()}
                <li>  Email: ${profile.getEmail()}
            </ul>
        `;
    //Id z profile.getId() sa nema pouzivat na komunikaciu s vlastnym serverom (you should not use the id from profile.getId() for communication with your server)
    document.getElementById("userStatus").innerHTML = htmlStringEn;
}

function renderLogOutInfo(htmlElmId) {
    const htmlString=
        `
                <p>User not signed in</p>
                `;
    document.getElementById(htmlElmId).innerHTML=htmlString;
}

function signOut() {
    document.getElementById("signOutButton").classList.add("hiddenElm");
    if(auth2.signOut) auth2.signOut();
    if(auth2.disconnect) auth2.disconnect();
    closeRatingClick();
    closeCommentClick();
}

function userChanged(user){
    document.getElementById("userName").innerHTML=user.getBasicProfile().getName();
    const userInfoElm = document.getElementById("userStatus");
    const userNameInputElm = document.getElementById("add-author");
    const userNameInputElm2 = document.getElementById("comment-author");
    const userNameInputElm3 = document.getElementById("op-username");

    if(userInfoElm ) {
        renderUserInfo(user, "userStatus");
    }
    if (userNameInputElm) {
        userNameInputElm.value = user.getBasicProfile().getName();
    }
    if (userNameInputElm2) {
        userNameInputElm2.value = user.getBasicProfile.getName();
    }
    if (userNameInputElm3) {
        userNameInputElm2.value = user.getBasicProfile.getName();
    }

}


function updateSignIn() {

    const sgnd = auth2.isSignedIn && auth2.isSignedIn.get();
    if(!sgnd){
        document.getElementById("signOutButton").classList.add("hiddenElm");
    }else{
        document.getElementById("signOutButton").classList.remove("hiddenElm");
    }
    const userNameInputElm3 = document.getElementById("userName");
    const userNameInputElm2 = document.getElementById("userName2");
    const userNameInputElm1 = document.getElementById("userName3");

    if (userNameInputElm3){
        if (sgnd) {
            value=auth2.currentUser.get().getBasicProfile().getName();
            userNameInputElm3.value = value.replace(/\s/g, '');
        }else{
            userNameInputElm3.value="";
        }
    }
    if (userNameInputElm2){
        if (sgnd) {
            value=auth2.currentUser.get().getBasicProfile().getName();
            userNameInputElm2.value = value.replace(/\s/g, '');
        }else{
            userNameInputElm2.value="";
        }
    }
    if (userNameInputElm1){
        if (sgnd) {
            value=auth2.currentUser.get().getBasicProfile().getName();
            userNameInputElm1.value = value.replace(/\s/g, '');
        }else{
            userNameInputElm1.value="";
        }
    }
}

function startGSingIn() {
    gapi.load('auth2', function() {
        gapi.signin2.render('my-signin2', {
            'width': 170,
            'height': 50,
            'longtitle': false,
            'theme': 'dark',
            'onsuccess': onSuccess,
            'onfailure': onFailure
        });
        gapi.auth2.init().then( //(called after OAuth 2.0 initialisation)
            function (){
                auth2 = gapi.auth2.getAuthInstance();
                auth2.currentUser.listen(userChanged);
                auth2.isSignedIn.listen(updateSignIn);
                auth2.then(updateSignIn); //(later after initialisation)
            });
    });
}

function onSuccess(googleUser) {
    document.getElementById('signOutButton').classList.remove("hiddenElm");
}
function onFailure(error) {
    console.log(error);
}