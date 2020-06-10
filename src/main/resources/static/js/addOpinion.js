document.getElementById("addOpinion").addEventListener("click", () =>{
    setTimeout(function(){
        addListenersToOpinionButts();
        updateSignIn();
    }, 1);
});

function addListenersToOpinionButts() {
    let resetButton = document.querySelector('#submitButton');
    let submitButton = document.querySelector('#resetButton');
    submitButton.addEventListener('mousedown', () => submitButton.style.backgroundColor = 'white');
    resetButton.addEventListener('mousedown', () => resetButton.style.backgroundColor = 'white');
    submitButton.addEventListener('mouseup', () => submitButton.style.backgroundColor = null);
    resetButton.addEventListener('mouseup', () => resetButton.style.backgroundColor = null);
}

function processData(event) {
    event.preventDefault();
    let url = 'https://parseapi.back4app.com/classes/opinions';
    const userName = document.getElementById("userName").value.trim();
    const email = document.getElementById("emailInput").value.trim();
    const urlInput = document.getElementById("url").value.trim();
    let zrkadlovkaInput;
    if (document.getElementById('yesAnswer').checked) {
        zrkadlovkaInput = true;
    }
    if (document.getElementById('noAnswer').checked) {
        zrkadlovkaInput = false;
    }
    let keywords = document.getElementById("keywords").value.trim();
    if(keywords !== "") {
        keywords = document.getElementById("keywords").value.trim().split(" ");
    }
    const userOpinion = document.getElementById("story").value.trim();
    const gdpr = document.getElementById("gdprInput").checked;
    if (userName === "" || userOpinion === "") {
        window.alert("Kolonky meno i názov sú povinné!");
        return;
    } else if (!gdpr) {
        window.alert("Ak chcete pridať komentár, musíte súhlasiť s podmienkami.");
        return;
    }
    let komentar = {
        userName: userName,
        email: email,
        url: urlInput,
        isPhotographing: zrkadlovkaInput,
        keywords: keywords,
        opinion: userOpinion,
        created: new Date()
    };
    console.log(komentar);
    if(!email){
        delete komentar.email;
    }
    if(!url){
        delete komentar.url;
    }
    if(zrkadlovkaInput == null){
        delete komentar.isPhotographing;
    }
    if(keywords === ""){
        delete komentar.keywords;
    }
    console.log(komentar);
    const init={
        headers: {
            "X-Parse-Application-Id": "8L091cvrHVEv3pXQmmddzTW6EZuaQEnEx6YxJOm7",
            "X-Parse-REST-API-Key": "NQGJO59tyMDooZaOFPQw4OGrD6KmU3aksoZzOoK0",
            "Content-Type": "application/json"
        },
        method: 'POST',
        body: JSON.stringify(komentar)
    };
    fetch(url,init)
        .then((response) => {
            if(response.ok){
                return response.json();
            }else{
                Promise.reject("FAIL");
            }
        })
        .then((responseJSON) =>{
            window.alert("Successfully added opinion!");
            window.location.hash="#opinions";
        })
        .catch((error) =>{
            console.log(error);
        })
}