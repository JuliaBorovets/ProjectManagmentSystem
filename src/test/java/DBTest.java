import com.training.demo.controllers.util.ProjectPasswordEncoder;
import com.training.demo.controllers.util.TestSetupConfig;
import com.training.demo.dto.*;
import com.training.demo.service.*;
import com.training.demo.repository.*;
import com.training.demo.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestSetupConfig.class })
@DataJpaTest
@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class DBTest {

    @Autowired
    ProjectRepository pr;
    @Autowired
    WorkerRepository wr;
    @Autowired
    TaskRepository tr;
    @Autowired
    ArtifactRepository ar;
    boolean exceptionThrown;

    final ProjectPasswordEncoder passwordEncoder = new ProjectPasswordEncoder();

    @BeforeEach
    void init(){
        ResourceDatabasePopulator rdp = new ResourceDatabasePopulator();
        rdp.setContinueOnError(true);
        rdp.addScript(new ClassPathResource("data.sql"));
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/proj_man_sys_test?useUnicode=true&serverTimezone=UTC&useSSL=true&verifyServerCertificate=false");
        ds.setUsername("miya");
        ds.setPassword("password");
        DatabasePopulatorUtils.execute(rdp, ds);
        exceptionThrown = false;
    }

    @Test
    void testEmptyProject(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);
        Assertions.assertEquals(0, ps.getAllProjects().size());

    }

    @Test
    void testCrtPrj(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);

        Worker worker1 = new Worker();
        worker1.setId((long)1);
        List<Task> task_l = new ArrayList<Task>();

        List<Project> prj_list = new ArrayList<Project>();
        Project prj_a = new Project();
        prj_a.setAdmin(worker1);
        prj_a.setName("prj1");
        prj_a.setDescription("testprj1");
        prj_a.setId((long)1);
        prj_a.setTasks(task_l);
        prj_list.add(prj_a);

        ProjectDTO pDTO = new ProjectDTO((long)1, "prj1", "testprj1", task_l);
        try{
            ps.saveNewProject(pDTO, worker1);
            Assertions.assertEquals(1, ps.getAllProjects().size());
            Assertions.assertEquals(prj_list.get(0).getId(), ps.getAllProjects().get(0).getId());
            Assertions.assertEquals(prj_list.get(0).getName(), ps.getAllProjects().get(0).getName());
            Assertions.assertEquals(prj_list.get(0).getDescription(), ps.getAllProjects().get(0).getDescription());
            Assertions.assertEquals(prj_list.get(0).getTasks(), ps.getAllProjects().get(0).getTasks());
        } catch(Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);

    }

    @Test
    void testCrtWorkerAndFindWorkerByID(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());

        Worker worker1 = new Worker();
        worker1.setId((long)1);
        List<Task> task_l = new ArrayList<Task>();
        WorkerDTO wDTO = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);

        Worker work_a = new Worker();
        work_a.setId((long)1);
        work_a.setName("Bill");
        work_a.setSurname("Miller");
        work_a.setLogin("bill_mill");
        work_a.setEmail("bill_mill@mail.com");
        work_a.setTasks(task_l);
        work_a.setAccountNonExpired(true);
        work_a.setAccountNonLocked(true);
        work_a.setCredentialsNonExpired(true);

        try{
            ws.saveNewWorker(wDTO);
            Worker work_a1 = ws.findWorkerById((long)1);
            Assertions.assertEquals(work_a.getId(), work_a1.getId());
            Assertions.assertEquals(work_a.getName(), work_a1.getName());
            Assertions.assertEquals(work_a.getSurname(), work_a1.getSurname());
            Assertions.assertEquals(work_a.getLogin(), work_a1.getLogin());
            Assertions.assertEquals(work_a.getEmail(), work_a1.getEmail());
        } catch(Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);

    }

    @Test
    void testDelPrj(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);

        List<Task> task_l = new ArrayList<Task>();
        Worker work_a = new Worker();
        work_a.setId((long)1);
        work_a.setName("Bill");
        work_a.setSurname("Miller");
        work_a.setLogin("bill_mill");
        work_a.setEmail("bill_mill@mail.com");
        work_a.setTasks(task_l);
        work_a.setAccountNonExpired(true);
        work_a.setAccountNonLocked(true);
        work_a.setCredentialsNonExpired(true);
        WorkerDTO wDTO = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);

        List<Project> prj_list = new ArrayList<Project>();
        Project prj_a = new Project();
        prj_a.setAdmin(work_a);
        prj_a.setName("prj1");
        prj_a.setDescription("testprj1");
        prj_a.setId((long)1);
        prj_a.setTasks(task_l);
        prj_list.add(prj_a);

        ProjectDTO pDTO = new ProjectDTO((long)1, "prj1", "testprj1", task_l);
        try{
            ws.saveNewWorker(wDTO);
            ps.saveNewProject(pDTO, ws.findWorkerById((long)1));
            Assertions.assertEquals(1, ps.getAllProjects().size());
            Project prj_actual = ps.getAllProjects().get(0);
            Assertions.assertEquals(prj_list.get(0).getId(), prj_actual.getId());
            Assertions.assertEquals(prj_list.get(0).getName(), prj_actual.getName());
            Assertions.assertEquals(prj_list.get(0).getDescription(), prj_actual.getDescription());
            Assertions.assertEquals(prj_list.get(0).getTasks(), prj_actual.getTasks());
            ps.deleteProject(prj_actual.getId());
            Assertions.assertEquals(0, ps.getAllProjects().size());
        } catch(Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);

    }

    @Test
    void testFindPrjByID(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);

        Worker worker1 = new Worker();
        worker1.setId((long)1);
        List<Task> task_l = new ArrayList<Task>();


        Project prj_exp = new Project();
        prj_exp.setAdmin(worker1);
        prj_exp.setName("prj3");
        prj_exp.setDescription("testprj3");
        prj_exp.setId((long)3);
        prj_exp.setTasks(task_l);


        ProjectDTO pDTO1 = new ProjectDTO((long)1, "prj1", "testprj1", task_l);
        ProjectDTO pDTO2 = new ProjectDTO((long)2, "prj2", "testprj2", task_l);
        ProjectDTO pDTO3 = new ProjectDTO((long)3, "prj3", "testprj3", task_l);
        try{
            ps.saveNewProject(pDTO1, worker1);
            ps.saveNewProject(pDTO2, worker1);
            ps.saveNewProject(pDTO3, worker1);
            Project prj_actual = ps.findProjectById((long)3);
            Assertions.assertEquals(prj_exp.getId(), prj_actual.getId());
            Assertions.assertEquals(prj_exp.getName(), prj_actual.getName());
            Assertions.assertEquals(prj_exp.getDescription(), prj_actual.getDescription());
            Assertions.assertEquals(prj_exp.getTasks(), prj_actual.getTasks());
        } catch(Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);

    }

    @Test
    void testAddWorkerToPrj(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);

        List<Task> task_l = new ArrayList<Task>();
        Worker worker1 = new Worker();
        worker1.setId((long)1);
        WorkerDTO wDTO1 = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);
        Worker worker2 = new Worker();
        worker2.setId((long)2);
        worker2.setLogin("sam_mill");
        WorkerDTO wDTO2 = new WorkerDTO((long)2, "Sam", "Miller","sam_mill","sam_mill@mail.com","password123", task_l);
        AddWorkerDTO awDTO = new AddWorkerDTO(worker2.getId(), worker2.getLogin());

        ArrayList prjDTO_exp = new ArrayList();
        ProjectDTO pDTO1 = new ProjectDTO((long)1, "prj1", "testprj1", task_l);
        prjDTO_exp.add(pDTO1);
        try{
            ws.saveNewWorker(wDTO1);
            ws.saveNewWorker(wDTO2);
            ps.saveNewProject(pDTO1, worker1);
            ps.addWorkerToProject(awDTO,(long)1);
            Worker newW = ws.findWorkerById((long)2);
            Assertions.assertEquals(1, newW.getProjects().size());
            Assertions.assertEquals(pDTO1.getId(), newW.getProjects().get(0).getId());
            Assertions.assertEquals(pDTO1.getName(), newW.getProjects().get(0).getName());
            Assertions.assertEquals(pDTO1.getDescription(), newW.getProjects().get(0).getDescription());

        } catch(Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);

    }

    @Test
    void testDelWorkerFromPrj(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);

        List<Task> task_l = new ArrayList<Task>();
        Worker worker1 = new Worker();
        worker1.setId((long)1);
        WorkerDTO wDTO1 = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);
        Worker worker2 = new Worker();
        worker2.setId((long)2);
        worker2.setLogin("sam_mill");
        WorkerDTO wDTO2 = new WorkerDTO((long)2, "Sam", "Miller","sam_mill","sam_mill@mail.com","password123", task_l);
        AddWorkerDTO awDTO = new AddWorkerDTO(worker2.getId(), worker2.getLogin());

        ArrayList prjDTO_exp = new ArrayList();
        ProjectDTO pDTO1 = new ProjectDTO((long)1, "prj1", "testprj1", task_l);
        prjDTO_exp.add(pDTO1);
        try{
            ws.saveNewWorker(wDTO1);
            ws.saveNewWorker(wDTO2);
            ps.saveNewProject(pDTO1, worker1);
            ps.addWorkerToProject(awDTO,(long)1);
            Worker newW = ws.findWorkerById((long)2);
            Assertions.assertEquals(1, newW.getProjects().size());
            Assertions.assertEquals(pDTO1.getId(), newW.getProjects().get(0).getId());
            Assertions.assertEquals(pDTO1.getName(), newW.getProjects().get(0).getName());
            Assertions.assertEquals(pDTO1.getDescription(), newW.getProjects().get(0).getDescription());
            ps.deleteWorkerFromProject(pDTO1.getId(), worker2.getId());
            Assertions.assertEquals(0, newW.getProjects().size());

        } catch(Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);

    }

    @Test
    void testFindPrjsByWorker(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);

        List<Task> task_l = new ArrayList<Task>();
        Worker worker1 = new Worker();
        worker1.setId((long)1);
        WorkerDTO wDTO1 = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);
        Worker worker2 = new Worker();
        worker2.setId((long)2);
        worker2.setName("Sam");
        worker2.setSurname("Miller");
        worker2.setLogin("sam_mill");
        worker2.setEmail("sam_mill@mail.com");
        WorkerDTO wDTO2 = new WorkerDTO((long)2, "Sam", "Miller","sam_mill","sam_mill@mail.com","password123", task_l);
        AddWorkerDTO awDTO = new AddWorkerDTO(worker2.getId(), worker2.getLogin());

        ArrayList prjDTO_exp = new ArrayList();
        ProjectDTO pDTO1 = new ProjectDTO((long)1, "prj1", "testprj1", task_l);
        ProjectDTO pDTO2 = new ProjectDTO((long)2, "prj2", "testprj2", task_l);
        ProjectDTO pDTO3 = new ProjectDTO((long)3, "prj3", "testprj3", task_l);
        prjDTO_exp.add(pDTO1);
        prjDTO_exp.add(pDTO2);
        prjDTO_exp.add(pDTO3);
        try{
            ws.saveNewWorker(wDTO1);
            ws.saveNewWorker(wDTO2);
            ps.saveNewProject(pDTO1, worker1);
            ps.saveNewProject(pDTO2, worker2);
            ps.saveNewProject(pDTO3, worker2);
            ps.addWorkerToProject(awDTO, pDTO1.getId());
            Assertions.assertEquals(3, ps.findProjectsByWorker(worker2));

        } catch(Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);

    }

    @Test
    void testWorkIsAdmin(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);

        Worker worker1 = new Worker();
        worker1.setId((long)1);
        List<Task> task_l = new ArrayList<Task>();
        WorkerDTO wDTO = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);

        Project prj_a = new Project();
        prj_a.setAdmin(worker1);

        ProjectDTO pDTO = new ProjectDTO((long)1, "prj1", "testprj1", task_l);
        try{
            ws.saveNewWorker(wDTO);
            ps.saveNewProject(pDTO, worker1);
            ws.isAdmin(worker1,prj_a);

        } catch(Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);

    }

    @Test
    void testFindWorkersByPrjID(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);

        List<Task> task_l = new ArrayList<Task>();
        Worker worker1 = new Worker();
        worker1.setId((long)1);
        WorkerDTO wDTO1 = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);
        Worker worker2 = new Worker();
        worker2.setId((long)2);
        worker2.setName("Sam");
        worker2.setSurname("Miller");
        worker2.setLogin("sam_mill");
        worker2.setEmail("sam_mill@mail.com");
        WorkerDTO wDTO2 = new WorkerDTO((long)2, "Sam", "Miller","sam_mill","sam_mill@mail.com","password123", task_l);
        AddWorkerDTO awDTO = new AddWorkerDTO(worker2.getId(), worker2.getLogin());

        ArrayList prjDTO_exp = new ArrayList();
        ProjectDTO pDTO1 = new ProjectDTO((long)1, "prj1", "testprj1", task_l);
        ProjectDTO pDTO2 = new ProjectDTO((long)2, "prj2", "testprj2", task_l);
        ProjectDTO pDTO3 = new ProjectDTO((long)3, "prj3", "testprj3", task_l);
        prjDTO_exp.add(pDTO1);
        prjDTO_exp.add(pDTO2);
        prjDTO_exp.add(pDTO3);
        try{
            ws.saveNewWorker(wDTO1);
            ws.saveNewWorker(wDTO2);
            ps.saveNewProject(pDTO1, worker1);
            ps.saveNewProject(pDTO2, worker2);
            ps.saveNewProject(pDTO3, worker2);
            List<WorkerDTO> wDTOs_exp = new ArrayList<>();
            wDTOs_exp.add(wDTO1);
            wDTOs_exp.add(wDTO2);
            ps.addWorkerToProject(awDTO, pDTO1.getId());
            List<WorkerDTO> wDTOs_actual = new ArrayList<>(ws.findWorkersByProjectId(ps.findProjectById(pDTO1.getId())));
            Assertions.assertEquals(wDTOs_exp.size(), wDTOs_actual.size());

        } catch(Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);

    }

    @Test
    void testAddArtfAndFindArtfByID(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);
        ArtifactService aS = new ArtifactService(ar, ps);

        ArrayList<Task> task_l = new ArrayList<>();
        Artifact artf_exp = new Artifact();
        artf_exp.setId((long)1);
        artf_exp.setName("doc1");
        artf_exp.setType("doc");
        artf_exp.setContent("documentation");
        ArtifactDTO aDTO = new ArtifactDTO((long)1, "doc1","doc","documentation");

        Worker worker1 = new Worker();
        worker1.setId((long)1);
        WorkerDTO wDTO = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);

        Project prj_a = new Project();
        prj_a.setAdmin(worker1);
        prj_a.setName("prj1");
        prj_a.setDescription("testprj1");
        prj_a.setId((long)1);
        prj_a.setTasks(task_l);
        ProjectDTO pDTO1 = new ProjectDTO((long)1, "prj1", "testprj1", task_l);

        try{
            ws.saveNewWorker(wDTO);
            ps.saveNewProject(pDTO1, worker1);
            aS.addArtifact(aDTO, prj_a);
            Artifact artf_actual = aS.findArtifactById((long)1);
            Assertions.assertEquals(artf_exp.getId(), artf_actual.getId());
            Assertions.assertEquals(artf_exp.getName(), artf_actual.getName());
            Assertions.assertEquals(artf_exp.getType(), artf_actual.getType());
            Assertions.assertEquals(artf_exp.getContent(), artf_actual.getContent());
        }catch (Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);
    }

    @Test
    void testDelArtf(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);
        ArtifactService aS = new ArtifactService(ar, ps);

        ArrayList<Task> task_l = new ArrayList<>();
        Artifact artf_exp = new Artifact();
        artf_exp.setId((long)1);
        artf_exp.setName("doc1");
        artf_exp.setType("doc");
        artf_exp.setContent("documentation");
        ArtifactDTO aDTO = new ArtifactDTO((long)1, "doc1","doc","documentation");

        Worker worker1 = new Worker();
        worker1.setId((long)1);
        WorkerDTO wDTO = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);

        Project prj_a = new Project();
        prj_a.setAdmin(worker1);
        prj_a.setName("prj1");
        prj_a.setDescription("testprj1");
        prj_a.setId((long)1);
        prj_a.setTasks(task_l);
        ProjectDTO pDTO1 = new ProjectDTO((long)1, "prj1", "testprj1", task_l);

        try{
            ws.saveNewWorker(wDTO);
            ps.saveNewProject(pDTO1, worker1);
            aS.addArtifact(aDTO, prj_a);
            Artifact artf_actual = aS.findArtifactById((long)1);
            Assertions.assertEquals(artf_exp.getId(), artf_actual.getId());
            Assertions.assertEquals(artf_exp.getName(), artf_actual.getName());
            Assertions.assertEquals(artf_exp.getType(), artf_actual.getType());
            Assertions.assertEquals(artf_exp.getContent(), artf_actual.getContent());
            aS.deleteArtifact(artf_actual.getId());
            Assertions.assertEquals(0, aS.findArtifactsByProjectId(prj_a.getId()).size());
        }catch (Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);
    }

    @Test
    void testFindArtfsByPrjID(){
        WorkerService ws = new WorkerService(wr, new ProjectPasswordEncoder());
        ProjectService ps = new ProjectService(pr, ws, wr, tr);
        ArtifactService aS = new ArtifactService(ar, ps);

        ArrayList<Task> task_l = new ArrayList<>();
        Artifact artf_exp = new Artifact();
        artf_exp.setId((long)1);
        artf_exp.setName("doc1");
        artf_exp.setType("doc");
        artf_exp.setContent("documentation");
        ArtifactDTO aDTO = new ArtifactDTO((long)1, "doc1","doc","documentation");
        ArtifactDTO aDTO1 = new ArtifactDTO((long)2, "doc2","doc","documentation");

        Worker worker1 = new Worker();
        worker1.setId((long)1);
        WorkerDTO wDTO = new WorkerDTO((long)1, "Bill", "Miller","bill_mill","bill_mill@mail.com","password", task_l);

        Project prj_a = new Project();
        prj_a.setAdmin(worker1);
        prj_a.setName("prj1");
        prj_a.setDescription("testprj1");
        prj_a.setId((long)1);
        prj_a.setTasks(task_l);
        ProjectDTO pDTO1 = new ProjectDTO((long)1, "prj1", "testprj1", task_l);

        try{
            ws.saveNewWorker(wDTO);
            ps.saveNewProject(pDTO1, worker1);
            aS.addArtifact(aDTO, prj_a);
            Assertions.assertEquals(1, aS.findArtifactsByProjectId((long)1).size());
            Artifact artf_actual = aS.findArtifactsByProjectId((long)1).get(0);
            Assertions.assertEquals(artf_exp.getId(), artf_actual.getId());
            Assertions.assertEquals(artf_exp.getName(), artf_actual.getName());
            Assertions.assertEquals(artf_exp.getType(), artf_actual.getType());
            Assertions.assertEquals(artf_exp.getContent(), artf_actual.getContent());
            aS.addArtifact(aDTO1, prj_a);
            Assertions.assertEquals(2, aS.findArtifactsByProjectId((long)1).size());
        }catch (Exception e) {
            exceptionThrown = true;
        }
        Assertions.assertEquals(false, exceptionThrown);
    }


}
