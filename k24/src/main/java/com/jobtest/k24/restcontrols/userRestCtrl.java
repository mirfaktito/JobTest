package com.jobtest.k24.restcontrols;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobtest.k24.models.BiodataModel;
import com.jobtest.k24.models.RegisterModal;
import com.jobtest.k24.models.UserJoinBiodataModel;
import com.jobtest.k24.models.UserModel;
import com.jobtest.k24.repository.UserRepo;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class userRestCtrl {

    @Autowired
    UserRepo repo;

    @PostMapping("/login")
    public ResponseEntity<Object> getLogin(@RequestBody UserModel userModel) {
        Map<String, Object> loginResult = repo.getlogin(userModel.getEmail(), userModel.getPassword());

        if (loginResult == null || loginResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email or password is incorrect");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(loginResult);
        }
    }

    @GetMapping("/biodata/{id}")
    public ResponseEntity<List<UserJoinBiodataModel>> getData(@PathVariable long id) {
        List<UserJoinBiodataModel> result = repo.getData(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterModal request) throws IOException {
                
        BiodataModel biodata = request.getBiodata();
        UserModel user = request.getUser();

        String name = biodata.getFullname();
        String nohp = biodata.getNohp();
        LocalDate dob = biodata.getDob();
        String jk = biodata.getJenisKelamin();
        Long noktp = biodata.getNoKtp();
        String email = user.getEmail();
        String password = user.getPassword();
        Long roleId = user.getRoleId();

        if (name == null || name.isEmpty() || nohp == null || nohp.isEmpty() ||
                dob == null || jk == null || jk.isEmpty() || noktp == null || email == null ||
                email.isEmpty() || password == null || password.isEmpty() || roleId == null ) {
            return ResponseEntity.badRequest().body("Data Tidak Lengkap");
        }

        repo.pendaftaran(name, nohp, dob, jk, noktp, email, password, roleId);
        return ResponseEntity.ok("Register Berhasil");
    }

    @GetMapping("/hak")
    public Page<UserJoinBiodataModel> getall(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "2") int size,
            @RequestParam(value = "cari", defaultValue = "") String cari) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.getall(pageable, cari);
    }

    @GetMapping("/hak2")
    public Page<UserJoinBiodataModel> getdata(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "2") int size,
            @RequestParam(value = "cari", defaultValue = "") String cari) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.getdata(pageable, cari);
    }

    @PostMapping("/tambahdata")
    public ResponseEntity<String> tambahdata(@RequestBody RegisterModal request) {
        BiodataModel biodata = request.getBiodata();
        UserModel user = request.getUser();

        String name = biodata.getFullname();
        String nohp = biodata.getNohp();
        LocalDate dob = biodata.getDob();
        String email = user.getEmail();

        if (name == null || name.isEmpty() || nohp == null || nohp.isEmpty() ||
                dob == null || email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Data Tidak Lengkap");
        }

        repo.tambahdata(name, nohp, dob, email);
        return ResponseEntity.ok("Register Berhasil");
    }

    @GetMapping("/byid/{id}")
    public UserJoinBiodataModel tamildata(@PathVariable long id) {
        return repo.tamipldata(id);
    }

    @PutMapping("/ubahbiodata/{id}")
    public Map<String, Object> ubahbiodata(@PathVariable long id,
            @RequestBody BiodataModel rm) {
        Map<String, Object> result = new HashMap<>();
        try {
            repo.ubahbiodata(rm.getFullname(), rm.getNohp(), rm.getDob(), id);
            result.put("result", "Edit Success");
        } catch (Exception e) {
            result.put("result", "Data tidak boleh kembar");
        }
        return result;
    }

    @PutMapping("/ubahuser/{id}")
    public Map<String, Object> ubahuser(@PathVariable long id,
            @RequestBody UserModel rm) {
        Map<String, Object> result = new HashMap<>();
        try {
            repo.ubahuser(rm.getEmail(), id);
            result.put("result", "Edit Success");
        } catch (Exception e) {
            result.put("result", "Data tidak boleh kembar");
        }
        return result;
    }

    @DeleteMapping("/dltbiodata/{id}")
    public void dltbiodata(@PathVariable long id) {
        repo.hapusbiodata(id);
    }

    @DeleteMapping("/dltuser/{id}")
    public void dltuser(@PathVariable long id) {
        repo.hapususer(id);
    }

    @GetMapping("/getlastid")
    public Long getidResetPass() {
        return repo.getlastid();
    }

    @PutMapping("/edtimg/{id}")
    public void upload(@PathVariable long id, @RequestParam("file") MultipartFile file) throws IOException {
        
        byte[] img = file.getBytes();
        repo.edtImg(img, id);
    }
}
