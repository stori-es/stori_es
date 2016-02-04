function focusSearch(obj) {
    obj.className = "search-active";
}
function blurSearch(obj) {
    if (obj.value == "") {
        obj.className = "search";
    }
}
function toggleLogMenu() {
    var logInMenuOpened = document.getElementById("sign-in-menu-openned");
    var logInMenuClosed = document.getElementById("sign-in-menu-closed");
    logInMenuOpened.style.width = jQuery("#sign-in-menu-closed").width() + 10 + ("px");
    if (logInMenuClosed.style.display == "block" || logInMenuClosed.style.display == "") {
        logInMenuOpened.style.display = "block";
        logInMenuClosed.style.display = "none";
    }
    else {
        logInMenuOpened.style.display = "none";
        logInMenuClosed.style.display = "block";
    }
}
function addInputSubmitEvent(e) {
    if (window.event) {
        e = window.event;
        if (e.keyCode == 13) {
            document.forms["login"].submit();
            return false;
        }
    }
}

/* seo-header script
 jQuery(document).ready(function() {
 jQuery("#seo-header-wrap > dd").bind("mouseenter", function(){
 jQuery("#seo-header-wrap > dd").css({backgroundPosition: ""});
 jQuery(".main-nav-wraps").dequeue().hide();
 if(this.className == "shopping" || this.className == "health"){
 jQuery("#main-nav-" + this.className.split(' ')[0]).css({'position' : 'absolute','top' : jQuery(this).position().top+40,'left' : jQuery(this).position().left-(308 - jQuery(this).width())});
 } else {
 jQuery("#main-nav-" + this.className.split(' ')[0]).css({'position' : 'absolute','top' : jQuery(this).position().top+40,'left' : jQuery(this).position().left});
 }
 jQuery("#main-nav-" + this.className.split(' ')[0]).css({opacity: 0, display:"block"});
 jQuery(this).css({backgroundPosition: "0 -46px"});
 jQuery("#main-nav-" + this.className.split(' ')[0]).animate({queue: false,opacity: 0.95});
 });
 jQuery("#main_nav").bind("mouseleave", function(){
 jQuery(".main-nav-wraps").dequeue().hide();
 jQuery("#main-nav-" + this.className.split(' ')[0]).show().animate({queue: false,opacity: 0}, 200, function() {
 jQuery("#main-nav-" + this.className.split(' ')[0]).css('display', "none");
 });
 jQuery("#seo-header-wrap > dd").css({backgroundPosition: ""});
 });
 jQuery(".nav-close-button").bind("click", function(){
 jQuery(".main-nav-wraps").dequeue().hide();
 })
 });
 */
