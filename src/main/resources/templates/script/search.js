let $search = document.querySelector('.github-nickname-search');

$search.addEventListener('click', getCommitDone);
function getCommitDone() {
    const gitHubNickname = document.getElementById('github-nickname').value;

    return fetch("http://localhost:8080/commits?namePattern=" + gitHubNickname, {
        method: "GET"
    })
        .then(res => res.json())
        .then(display)
        .catch(err => alert("문제가 생겨버렸어용", err));
}

function display(res) {
    $commitHistoryTableRow.innerHTML = res.data.map(
        item => `<tr>
                        <td>${item.githubName}</td>
                        <td>${item.done}</td>
                    </tr>`).join('');
}
