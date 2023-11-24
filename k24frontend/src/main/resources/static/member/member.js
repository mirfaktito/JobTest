$(document).ready(function () {
    
     //logout
     $('#logout').click(function () {
        var now = new Date().getTime();
        window.open('/logout/', '_self')
        sessionStorage.clear()
        objlogin.last = now
    })

    isitable(0,5,'')
    function isitable(page, size, cari) {
        
        $.ajax({
            url: 'http://localhost:81/api/user/hak2/?page=' + page + '&size='+ size + '&cari=' + cari,
            type: 'GET',
            success: function (data) {
                var txt = ''
                txt += '<table class="table"><thead><tr>'
                txt += '<th>No</th><th>Nama</th><th>dob</th><th>NoHp</th><th>Email</th>'
                txt += '</tr></thead><tbody>'

                var count = page * size + 1;

                for (let i = 0; i < data.content.length; i++) {
                    txt += '<tr>'
                    txt += '<td>'+ count++ +'</td>'
                    txt += '<td>' + data.content[i].fullname + '</td>'
                    txt += '<td>' + data.content[i].dob + '</td>'
                    txt += '<td>' + data.content[i].nohp + '</td>'
                    txt += '<td>' + data.content[i].email + '</td>'
                    txt += '</tr>'
                }
                txt += '</tbody></table>'
                $('#dataAkses').html(txt)

                var tpages = data.totalPages
                ptxt = ''
                if (data.totalElements != 0) {
                    ptxt += '<button class="btn info"> << </button>'
                    for (p = 1; p <= tpages; p++) {
                        if (p == page + 1) {
                            ptxt += ' <button class="btn info pagingBtn active" value="' + p + '" > ' + p + ' </button> ';
                        } else {
                            ptxt += ' <button class="btn info pagingBtn" value="' + p + '" > ' + p + ' </button> ';
                        }
                    }
                    ptxt += '<button class="btn info"> >> </button>'
                } else if (data.totalElements == 0) {
                    ptxt += '<b> Not Found </b>'
                }
                $('#page').html(ptxt)

                $('.pagingBtn').click(function () {
                    var page = $(this).val() - 1
                    var cr = sessionStorage.getItem('cari')
                    if (cr == null) { cr = '' }
                    isitable(page, 5, cr)
                })

            }
        })
    }

    datalogin()
    function datalogin() {
        let idr = sessionStorage.getItem('idr')
        $.ajax({
            url: 'http://localhost:81/api/user/biodata/' + idr,
            type: 'GET',
            success: function (data) {
                var txt = ''
                txt += '<table class="table"><thead><tr>'
                txt += '<th>No</th><th>Nama</th><th>dob</th><th>NoHp</th><th>Email</th>'
                txt += '</tr></thead><tbody>'

                var count =+ 1;

                    txt += '<tr>'
                    txt += '<td>'+ count++ +'</td>'
                    txt += '<td>' + data[0].fullname + '</td>'
                    txt += '<td>' + data[0].dob + '</td>'
                    txt += '<td>' + data[0].nohp + '</td>'
                    txt += '<td>' + data[0].email + '</td>'
                    txt += '</tr>'
                txt += '</tbody></table>'
                
                $('#datalogin').html(txt)

            }
        })
    }

})