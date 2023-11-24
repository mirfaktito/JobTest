insert into biodata (fullname, dob, jenis_kelamin, no_ktp, image, nohp) values
('Ahmad Basuki', '1999-05-19','Laki-laki', '435678909', '', '0987644353'),
('Cahyo', '1999-06-14','Laki-laki', '435678909', '', '095313124'),
('Nugroho', '1999-03-19','Laki-laki', '215415413', '', '098735131'),
('Purnomo', '1999-08-18','Laki-laki', '32168463215', '', '098323512'),
('Denis', '1999-05-23','Perempuan', '84321351', '', '098352135'),
('Wulan', '1999-03-1','Perempuan', '1513156561', '', '0985614351'),
('Akmal', '1999-02-19','Laki-laki', '153115651', '', '0985454164'),
('Paisal', '1999-07-19','Laki-laki', '4112354564', '', '09868456351')
;

insert into user_data (biodata_id, role_id, email, password)
values
    (1, 1, 'ahmad@email.com', 'password1'),
    (2, 2, 'cahyo@email.com', 'password2'),
    (3, 2, 'Nugroho@email.com', 'password3'),
    (4, 2, 'purnomo@email.com', 'password4'),
    (5, 2, 'denis@email.com', 'password5'),
    (6, 2, 'wulan@email.com', 'password6'),
    (7, 2, 'akmal@email.com', 'password7'),
    (8, 2, 'paisal@email.com', 'password8')
    ;

insert into m_role (name) values
('admin'),('member')