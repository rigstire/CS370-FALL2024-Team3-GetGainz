document.addEventListener("DOMContentLoaded", () => {
	onLoaded();
});

function onLoaded() {
	// Update the navbar with the user's name if logged in
	const username = sessionStorage.getItem("username"); // or use localStorage

	if (username) {
		const loggedInNameElement = document.getElementById("logged-in-name");
		loggedInNameElement.textContent = `Logged in as: ${username}`; // Update the text with the username
	}
}

function onLogin() {
	let name = document.forms["loginForm"]["name"].value.trim();
	let password = document.forms["loginForm"]["password"].value.trim();

	let url = "/login/perform_login";
	let data = { "name": name, "password": password };

	let xhr = new XMLHttpRequest();
	xhr.open("POST", url);
	xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
	xhr.send(JSON.stringify(data));

	xhr.onreadystatechange = function() {
		if (this.readyState == XMLHttpRequest.DONE) {
			if (this.status == 200) {
				// SUCCESS: Store the username in sessionStorage
				sessionStorage.setItem("username", name);  // Save username in sessionStorage
				showStatus("Logged in successfully.");
				window.location.href = "/";  // Redirect to the home page
			} else {
				// ERROR: Display error message
				showStatus(this.responseText);
				clearLoginForm();
			}
		}
	}
}

function clearLoginForm() {
	clearFormValue("loginForm", "name");
	clearFormValue("loginForm", "password");
}

function logout() {
	sessionStorage.removeItem("username"); // Remove the username from sessionStorage
	window.location.href = "/login"; // Redirect to login page
}

function showStatus(message) {
	// Add logic here to show status message to the user
	console.log(message);
}

function clearFormValue(formId, fieldName) {
	const form = document.getElementById(formId);
	if (form) {
		form[fieldName].value = ''; // Clear form input
	}
}
