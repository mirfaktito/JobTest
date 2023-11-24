package com.jobtest.k24.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jobtest.k24.models.UserJoinBiodataModel;
import com.jobtest.k24.models.UserModel;

@Transactional
public interface UserRepo extends JpaRepository<UserModel, Long> {

    @Query(value = "SELECT mb.fullname, mb.id, mu.role_id " +
            " FROM user_data mu " +
            " JOIN biodata mb ON mb.id = mu.biodata_id " +
            " where mu.email=:em and mu.password=:pw ", nativeQuery = true)
    Map<String, Object> getlogin(String em, String pw);

    @Query(value = "select b.id, b.fullname, b.dob, b.nohp, ud.email from biodata b " + 
            " left join user_data ud on ud.biodata_id = b.id " + 
            " where b.id=:id ",nativeQuery = true)
    void getdata(long id);

    @Query(value = "SELECT b.id, b.fullname, b.dob, b.nohp, ud.email " +
           "FROM biodata b " +
           "LEFT JOIN user_data ud ON ud.biodata_id = b.id " +
           "WHERE b.id = :id ", nativeQuery = true)
    List<UserJoinBiodataModel> getData(long id);

    @Modifying
    @Query(value = "with daftar as ( " +
            " insert into biodata (fullname, nohp,dob,jenis_kelamin,no_ktp) " +
            " values (:name, :nohp, :dob, :jk, :noktp) " +
            " returning id " +
            " ) " +
            " insert into user_data (biodata_id, email, password, role_id) " +
            " values ((select daftar.id from daftar), :email, :password, :roleid) ", nativeQuery = true)
    void pendaftaran(String name, String nohp, LocalDate dob, String jk, Long noktp, String email, String password,
            Long roleid);

    @Query(value = "select b.id, b.fullname, b.dob, b.nohp, ud.email from biodata b " +
            " left join user_data ud on ud.biodata_id = b.id " +
            " where (b.fullname ilike %:cari%) order by b.id", nativeQuery = true)
    Page<UserJoinBiodataModel> getall(Pageable pageable, String cari);

    @Query(value = "select b.id, b.fullname, b.dob, b.nohp, ud.email from biodata b " +
            " left join user_data ud on ud.biodata_id = b.id " +
            " where (b.fullname ilike %:cari%) order by b.fullname", nativeQuery = true)
    Page<UserJoinBiodataModel> getdata(Pageable pageable, String cari);

    @Modifying
    @Query(value = "with tambah as ( " +
            " insert into biodata (fullname, nohp,dob) " +
            " values (:name, :nohp, :dob) " +
            " returning id " +
            " ) " +
            " insert into user_data (biodata_id, email) " +
            " values ((select tambah.id from tambah), :email) ", nativeQuery = true)
    void tambahdata(String name, String nohp, LocalDate dob, String email);

    @Query(value = "select b.id, b.fullname, b.dob, b.nohp, ud.email from biodata b " +
            " left join user_data ud on ud.biodata_id = b.id " +
            " where b.id = :id order by b.id ", nativeQuery = true)
    UserJoinBiodataModel tamipldata(long id);

    @Modifying
    @Query(value = " update biodata set fullname = :name, nohp = :nohp, dob = :dob " +
            " WHERE id = :id", nativeQuery = true)
    void ubahbiodata(String name, String nohp, LocalDate dob, long id);

    @Modifying
    @Query(value = "update user_data set email = :email " +
            " WHERE biodata_id = :id", nativeQuery = true)
    void ubahuser(String email, long id);

    @Modifying
    @Query(value = "DELETE FROM biodata " +
            " WHERE id = :id ", nativeQuery = true)
    void hapusbiodata(long id);

    @Modifying
    @Query(value = "DELETE FROM user_data " +  
            " WHERE id = :id ", nativeQuery = true)
    void hapususer(long id);

    @Query(value = "select max(id) from biodata", nativeQuery = true)
    Long getlastid();

    @Modifying
    @Query(value = "update biodata set image=:img where id=:id", nativeQuery = true)
    void edtImg(byte[] img, long id);
}
