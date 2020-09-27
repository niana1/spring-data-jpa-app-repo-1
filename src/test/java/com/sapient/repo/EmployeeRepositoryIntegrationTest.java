package com.sapient.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sapient.training.app.SpringDataJpaAppApplication;
import com.sapient.training.data.EmployeeRepository;
import com.sapient.training.entity.Employee;



//@RunWith(SpringRunner.class)
//@ExtendWith is JUnit5 version for JUnit4 @RunWith
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringDataJpaAppApplication.class })
//to use the same data source as your regular application 
//i.e to run DataJpaTest with PostgreSQL. Without the below annotation 
//and with only @DataJpaTest, testing framework configures a
//in-memory database by default
@AutoConfigureTestDatabase(replace=Replace.NONE)
/*
 * The @DataJpaTest uses @Transactional under the hood. 
 * A test is wrapped inside a transaction that is rolled back at the end. 
 * This means that when using e.g. Hibernate one needs to pay special 
 * attention to how the tested code is written. 
 * As shown below, a manual flush is indeed required:
 */
@DataJpaTest
public class EmployeeRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        Employee alex = new Employee("alex");
        entityManager.persistAndFlush(alex);

        Employee found = employeeRepository.findByName(alex.getName());
        assertThat(found.getName()).isEqualTo(alex.getName());
    }

    @Test
    public void whenInvalidName_thenReturnNull() {
        Employee fromDb = employeeRepository.findByName("doesNotExist");
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenFindById_thenReturnEmployee() {
        Employee emp = new Employee("test");
        entityManager.persistAndFlush(emp);

        Employee fromDb = 
        		employeeRepository
        		.findById(emp.getId()).orElse(null);
        assertThat(fromDb.getName()).isEqualTo(emp.getName());
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        Employee fromDb = employeeRepository
        		.findById(-11l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void givenSetOfEmployees_whenFindAll_thenReturnAllEmployees() {
        Employee alex = new Employee("alex");
        Employee ron = new Employee("ron");
        Employee bob = new Employee("bob");

        entityManager.persist(alex);
        entityManager.persist(bob);
        entityManager.persist(ron);
        entityManager.flush();

        List<Employee> allEmployees = 
        		employeeRepository.findAll();

        assertThat(allEmployees)
        .hasSize(3)
        .extracting(Employee::getName).containsOnly(alex.getName(), ron.getName(), bob.getName());
    }
        @Test
	public void givenSetOfEmployees_whenfindByJobAndSalary_thenReturnEmployee() throws ParseException {
		Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/01/01");
		Employee alex = new Employee("alex", date1, "manager", 12000.0);
		Employee ron = new Employee("ron", date1, "clerk", 11000.0);
		Employee don = new Employee("don", date1, "clerk", 11000.0);

		entityManager.persist(alex);
		entityManager.persist(ron);
		entityManager.persist(don);
		entityManager.flush();

		List<Employee> employees = employeeRepository.findByJobAndSalary("clerk", 11000.0);
		assertThat(employees).extracting(Employee::getName).containsOnly(ron.getName(), don.getName());
	}

	@Test
	public void givenSetOfEmployees_whenfindByJobAndSalary_whenInvalidJobAndSalary_thenReturnNull()
			throws ParseException {
		Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/01/01");
		Employee alex = new Employee("alex", date1, "manager", 12000.0);
		entityManager.persist(alex);
		entityManager.flush();

		List<Employee> employees = employeeRepository.findByJobAndSalary("manager", -11000.0);
		assertThat(employees).hasSize(0);
		employees = employeeRepository.findByJobAndSalary("clerk", -11000.0);
		assertThat(employees).hasSize(0);
	}

	@Test
	public void givenSetOfEmployees_findByJobAndSalaryGreaterThan_thenReturnEmployees() throws ParseException {
		Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/01/01");
		Employee alex = new Employee("alex", date1, "manager", 12000.0);
		Employee ron = new Employee("ron", date1, "clerk", 10000.0);
		Employee don = new Employee("don", date1, "clerk", 11000.0);

		entityManager.persist(alex);
		entityManager.persist(ron);
		entityManager.persist(don);
		entityManager.flush();

		List<Employee> employees = employeeRepository.findByJobAndSalaryGreaterThan("clerk", 9000.0);

		assertThat(employees).extracting(Employee::getName).containsOnly(ron.getName(), don.getName());
	}

	@Test
	public void givenSetOfEmployees_findByJobAndSalaryGreaterThan_whenInvalidJobAndSalary_thenReturnNull()
			throws ParseException {
		Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/01/01");
		Employee alex = new Employee("alex", date1, "manager", 12000.0);
		entityManager.persistAndFlush(alex);

		List<Employee> employees = employeeRepository.findByJobAndSalaryGreaterThan("manager", 13000.0);
		assertThat(employees).hasSize(0);

		employees = employeeRepository.findByJobAndSalaryGreaterThan("clerk", 110000.0);
		assertThat(employees).hasSize(0);

	}
}