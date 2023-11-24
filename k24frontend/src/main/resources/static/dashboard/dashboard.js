$(document).ready(function () {

    $('.loginbtn').click(function () {
        obj = {}
        obj.email = $('#InputEmail').val()
        obj.password = $('#InputPassword').val()
        var dJson = JSON.stringify(obj)
        console.log(obj.email)
        $.ajax({
            url: 'http://localhost:81/api/user/login',
            type: 'Post',
            contentType: 'application/json',
            data: dJson,
            success: function (data) {
            var idr = data.id
            sessionStorage.setItem('idr', idr)

            if (data.role_id == 1) {
                sessionStorage.setItem('idr',idr)
                window.open("/setsessionadmin/" + data.fullname, '_self')
            }else if (data.role_id == 2) {
                sessionStorage.setItem('idr',idr)
                window.open("/setsessionmember/" + data.fullname, '_self')
            }
            },
            error: function (xhr) {
                var errorMessage = xhr.responseText
                var mandatori = '<p style="color: red; font-style: italic; font-size:small;">' + '*' + errorMessage + '</p>'
                $('#mandatori').html(mandatori)
            }
        })
    })

     //logout
     $('#logout').click(function () {
        var now = new Date().getTime();
        window.open('/logout/', '_self')
        sessionStorage.clear()
        objlogin.last = now
    })

    isitable(0,2,'')
    function isitable(page, size, cari) {
        
        $.ajax({
            url: 'http://localhost:81/api/user/hak/?page=' + page + '&size='+ size + '&cari=' + cari,
            type: 'GET',
            success: function (data) {
                var txt = ''
                txt += '<table class="table"><thead><tr>'
                txt += '<th>No</th><th>Nama</th><th>dob</th><th>NoHp</th><th>Email</th>'
                txt += '</tr></thead><tbody>'

                var count = page * size + 1;

                for (let i = 0; i < data.content.length; i++) {
                    txt += '<tr>'
                    txt += '<td>' + count++ + '</td>'
                    txt += '<td>' + data.content[i].fullname + '</td>'
                    txt += '<td>' + data.content[i].dob + '</td>'
                    txt += '<td>' + data.content[i].nohp + '</td>'
                    txt += '<td>' + data.content[i].email + '</td>'
                    txt += '<td><button class="modalUbah btn btn-warning" value="U" name= "' + data.content[i].id + '"> U </button>'
                    txt += ' | '
                    txt += '<button class="modalHapus btn btn-danger" value="H" name= "' + data.content[i].id + '"> H </button></td>'
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
                    isitable(page, 2, cr)
                })

                // Show Modal Ubah
                $('.modalUbah').click(function () {
                     // Mengambil id
                     editId = $(this).attr('name')
                     sessionStorage.setItem('editIds', editId)
                    $('#modalUbah').modal("show")
                    // untuk mengisi input 
                    isiEdit()
                })

                // Show Modal hapus
                $('.modalHapus').click(function () {
                     // Mengambil id
                     dltId = $(this).attr('name')
                     sessionStorage.setItem('dlttIds', dltId)
                    $('#modalHapus').modal("show")
                    // untuk mengisi input 
                    isiHapus()
                })

            }
        })
    }

    // Untuk Mmengisi Kolom pada modal Ubah
    function isiEdit() {
        var editIds = sessionStorage.getItem('editIds')
        $.ajax({

            url: 'http://localhost:81/api/user/byid/' + editIds,
            type: 'GET',
            success: function (data) {
                // console.log(data)
                $('#Uname').val(data.fullname)
                $('#Udob').val(data.dob)
                $('#Unohp').val(data.nohp)
                $('#Uemail').val(data.email)

            }
        })
    }

    function isiHapus() {
        var dltId = sessionStorage.getItem('dlttIds')
        $.ajax({
            url: 'http://localhost:81/api/user/byid/' + dltId,
            type: 'GET',
            success: function (data) {
                $('#Hapusname').html(data.fullname)
            }
        })
    }

    // Untuk Button Cari
    $('#inputcari').eq(0).keypress(function (e) {
        var key = e.which;
        if (key == 13)  // the enter key code
        {
            sessionStorage.setItem('cari', $(this).val())
            var cari = $(this).val()
            isitable(0, 2, cari)
        }
    });

    $('#btnTambah').click(function () {
        $("#modalTambahRole").modal("show");
    })

    // Button Tambah data
    $('#btntambahSimpan').click(function () {
        var biodata = {
            fullname : $('#Tname').val(),
            dob : $('#Tdob').val(),
            nohp : $('#Tnohp').val()
        }
        var user = {
         email : $('#Temail').val()
        }
        var requestData = {
            biodata: biodata,
            user: user
        }
        var dJson = JSON.stringify(requestData)
        $.ajax({
            url: 'http://localhost:81/api/user/tambahdata',
            type: 'POST',
            contentType: 'application/json',
            data: dJson,
            success: function (data) {
                $('#modalTambahRole').modal('hide')
                $('#Tname').html('')
                $('#Tdob').html('')
                $('#Tnohp').html('')
                $('#Temail').html('')
                console.log("Tambah Data Berhasil")
            }
        });
    })

     // Button Simpan Pada Ubah Role
    $('#btnSimpanUbah').click(function () {
        ubahdatauser()
        ubahbiodata()
        location.reload()
        $("#modalUbah").modal("hide")
    })

    function ubahdatauser() {
        var editIds = sessionStorage.getItem('editIds')
        obj = {}
        obj.email = $('#Uemail').val()
        var dJson = JSON.stringify(obj)
        $.ajax({
            url: "http://localhost:81/api/user/ubahuser/" + editIds,
            type: "PUT",
            contentType: "application/json",
            data: dJson,
            success: function (data) {
                console.log("Ubah Data User Berhasil")
            }
        })
    }
    function ubahbiodata() {
        var editIds = sessionStorage.getItem('editIds')
        obj = {}
        obj.fullname = $('#Uname').val(),
        obj.dob = $('#Udob').val(),
        obj.nohp = $('#Unohp').val()
        var dJson = JSON.stringify(obj)
        $.ajax({
            url: "http://localhost:81/api/user/ubahbiodata/" + editIds,
            type: "PUT",
            contentType: "application/json",
            data: dJson,
            success: function (data) {
                console.log("Ubah Biodata Berhasil")
            }
        })
    }

    // Button Delete Pada Hapus Role
    $('#btnSimpanHapus').click(function () {
        hapusbiodata()
        hapususer()
        location.reload()
        $("#modalHapus").modal("hide")
    })

    function hapusbiodata() {
        var dltId = sessionStorage.getItem('dlttIds')
        $.ajax({
            url: 'http://localhost:81/api/user/dltbiodata/' + dltId,
            type: 'DELETE',
            contentType: 'application/json',
            success: function (data) {
                console.log("Hapus Biodata Berhasil")
            }
        })
    }

    function hapususer() {
        var dltId = sessionStorage.getItem('dlttIds')
        $.ajax({
            url: 'http://localhost:81/api/user/dltuser/' + dltId,
            type: 'DELETE',
            contentType: 'application/json',
            success: function (data) {
                console.log("Hapus User Berhasil")
            }
        })
    }

    // Pendaftaran
    $('.regisbtn').click(function () {
    var roleids = 2
    var biodata = {
        fullname: $('#namaMember').val(),
        nohp: $('#nohp').val(),
        dob: $('#dob').val(),
        jenisKelamin: $('#jenisKelamin').val(),
        noKtp: $('#noKtp').val()
    }
    var user = {
        email: $('#email').val(),
        password: $('#password').val(),
        roleId: roleids
    }
    var requestData = {
        biodata: biodata,
        user: user
    }
    var dJson = JSON.stringify(requestData)
    $.ajax({
        url: 'http://localhost:81/api/user/register',
        type: 'POST',
        contentType: 'application/json',
        data: dJson,
        success: function(response) {
            console.log(response);
            alert("data berhasil didaftarkan");
            window.open("/login", '_self')
        }
    });    
    })

    $('#btntambahBatal, #btnBatalUbah, #btnBatalHapus').click(function () {
        $.ajax({
            url: 'http://localhost:80/hak',
            type: 'GET',
            dataType: 'html',
            success: function (data) {
                $("#modalHapusRole").modal("hide");
                $("#modalUbahRole").modal("hide");
                $("#modalTambahRole").modal("hide");
                location.reload()
            }
        })
    })
})