document.addEventListener("DOMContentLoaded", function () {
    const uploadButton = document.getElementById("uploadButton");

    // Handle upload button click
    uploadButton.addEventListener("click", async () => {
        const fileInput = document.getElementById("fileInput");
        const files = fileInput.files;

        if (!files.length) {
            alert("Please select a photo to upload.");
            return;
        }

        const formData = new FormData();
        formData.append("file", files[0]);

        try {
            const response = await fetch("/photos/upload", {
                method: "POST",
                body: formData,
            });

            if (response.ok) {
                alert(await response.text());
                loadPhotos(); // Reload the album
            } else {
                alert("Upload failed due to incorrect file upload or the size is too large.");
            }
        } catch (error) {
            console.error(error);
            alert("An error occurred during upload.");
        }
    });

    // Load photos into the album
    async function loadPhotos() {
        const album = document.getElementById("album");
        album.innerHTML = ""; // Clear album before adding photos

        try {
            const response = await fetch("/photos/list");
            const photos = await response.json();

            photos.forEach((filename) => {
                const img = document.createElement("img");
                img.src = `/photos/${filename}`;
                img.alt = filename;
                img.style.width = "150px";
                img.style.height = "150px";
                img.style.objectFit = "cover";
                img.classList.add("border", "rounded");

                album.appendChild(img);
            });
        } catch (error) {
            console.error("Error loading photos:", error);
            alert("Failed to load photos.");
        }
    }

    loadPhotos(); // Load photos on page load
});
