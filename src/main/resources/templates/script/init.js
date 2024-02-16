let $commitHistoryTableRow = document.querySelector('.commit-history-table-row');
let $title = document.querySelector('.title1');

document.addEventListener('DOMContentLoaded', function() {
    getCommitDone();

    function getCommitDone() {
        return fetch("http://localhost:8080/commits", {
            method: "GET"
        })
            .then(res => res.json())
            .then(display)
            .catch(err => alert("문제가 생겨버렸어용", err));
    }
    function display(res) {
        console.log(res);
        $title.innerHTML = `${res.today} 커밋 여부`;

        $commitHistoryTableRow.innerHTML = res.data.map(
            item => `<tr>
                        <td>${item.githubName}</td>
                        <td>${item.done}</td>
                    </tr>`).join('');
    }
});



