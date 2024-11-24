document.addEventListener("DOMContentLoaded", () => {
	onLoaded();
	onHistory();
});

function onLoaded() {
	console.log("Page loaded.");
	showUserName();
}

function onLogout() {
	window.location = "/logout";
}

function onExercises() {
	hideStatus();
	hideDiv("History");
	hideDiv("Account");
	showDiv("Exercises");
	hideDiv("PhotoAlbum");
	showExercises();
}

function onHistory() {
	hideStatus();
	hideDiv("Exercises");
	hideDiv("Account");
	showDiv("History");
	hideDiv("PhotoAlbum");
	resetCalendar();
	showHistory();
}

function onAccount() {
	hideStatus();
	hideDiv("History");
	hideDiv("Exercises");
	hideDiv("PhotoAlbum");
	showDiv("Account");
	showAccount();
}
/*function onMealLog()
{
	hideStatus();
	hideDiv("History");
	hideDiv("Exercises");
	showDiv("MealLog");
}*/
function onPhotoAlbum()
{
	hideStatus();
	hideDiv("History");
	hideDiv("Exercises");
	hideDiv("MealLog");
	hideDiv("Account");
	showDiv("PhotoAlbum");
}

function hideDiv(id) {
	const element = document.getElementById(id);
	if (element) {
		element.style.display = "none";
	}
}

function showDiv(id) {
	const element = document.getElementById(id);
	if (element) {
		element.style.display = "block";
	}
}
