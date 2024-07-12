document.addEventListener('keydown', function(event) {
    // Отправка запроса на сервер
    fetch('/cubemaze/move', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;',
        },
        body: `direction=${event.key}`
    })
        .then(response => response.text())
        .then(html => {
            document.querySelector('.game-container').innerHTML = html;
        })
        .catch(error => console.error('Error:', error));
});