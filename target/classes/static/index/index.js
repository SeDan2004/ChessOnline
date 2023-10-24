let main = $("main")[0];

let authAndRegChoice = $(".auth_and_reg_choice")[0],
    authBtn = $(".auth_btn")[0],
    regBtn = $(".reg_btn")[0];

let auth = $(".auth")[0];

let reg = $(".reg")[0];

let forms = new Map();

forms.set(authBtn, auth);
forms.set(regBtn, reg);

function preventKeyboardMobileChange() { 
  setTimeout(() => {
	if (device.mobile()) {
	  if (screen.orientation.type === "landscape-primary") {
	    main.style.transform = "scale(0.6)";
	  }
	  
	  if (screen.orientation.type === "portrait-primary") {
	    if (main.style.transform !== "") {
		  main.style.transform = "scale(1)";
		}
	  }
	}
	
    document.body.style.height = document.body.clientHeight;
  }, 100);
}

function acceptForm() {
  let activeForm = $(".active_form")[0],
      formFields = [...activeForm.querySelectorAll("input")];
  
  emptyFieldsLen = formFields.filter(inp => inp.type !== "checkbox")
                             .map(inp => inp.value.trim());
                             
  if (emptyFieldsLen.includes("")) {
    alert("Есть незаполненные поля!");
    return;
  }
  
  if (activeForm.classList.contains("auth")) {
	function getAuthFormFieldsValues(inp) {
	  return inp.type === "checkbox" ? inp.checked : inp.value;
	}
	
    [login, pass, rememberMe] = formFields.map(getAuthFormFieldsValues);
    
    $.ajax({
	  method: "POST",
	  url: "auth",
	  data: {login: login, password: pass, rememberMe: rememberMe},
	  success(response) {
	    if (response.startsWith("error")) {
		  alert(response);
		  return;
		} else {
		  location.href += "lobby";
		}
	  }
	})
  }
  
  if (activeForm.classList.contains("reg")) {
    [login, pass, repeatPass] = formFields.map(inp => inp.value);
    
    if (pass !== repeatPass) {
	  alert("Пароли не совпадают!");
	  return;
	}
	
	$.ajax({
	  method: "POST",
	  url: "reg",
	  data: {login: login, password: pass},
	  success(response) {
	    if (response.startsWith("error")) {
		  alert(response);
		  return;
		} else {
		  location.href += "lobby";
		}
	  }
	})
  }
}

function showForm() {
  let form = forms.get(this),
      activeForm = $(".active_form")[0];
  
  if (this.classList.contains("back")) {
    this.removeEventListener("click", showForm);
    
    this.nextElementSibling
        .removeEventListener("click", acceptForm);
        
    forms.delete(this);
  }
  
  function changeFormClass() {
    activeForm.classList.remove("active_form");
    
    TweenMax.to(form, 0.5, {opacity: 1, onStart: () => {
	  form.classList.add("active_form");
		
	  if (!form.classList.contains("auth_and_reg_choice")) {
	    let backBtn = form.querySelector(".back"),
	        acceptBtn = backBtn.nextElementSibling;
	        
	    forms.set(backBtn, authAndRegChoice);
	    
	    backBtn.addEventListener("click", showForm);
	    acceptBtn.addEventListener("click", acceptForm);
	  }
    }})
  }
  
  TweenMax.to(activeForm, 0.5, {opacity: 0, onComplete: changeFormClass});
}

function addEvent() {
  if (device.mobile() || device.tablet()) {
    window.addEventListener("load", preventKeyboardMobileChange);
    window.addEventListener("orientationchange", preventKeyboardMobileChange);
  }
  
  authBtn.addEventListener("click", showForm);
  regBtn.addEventListener("click", showForm);  	
}

addEvent();