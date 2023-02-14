package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Java разработчик уровня Intern"));
        save(new Vacancy(0, "Junior Java Developer", "Java разработчик уровня Junior"));
        save(new Vacancy(0, "Junior+ Java Developer", "Java разработчик уровня Junior+"));
        save(new Vacancy(0, "Middle Java Developer", "Java разработчик уровня Middle"));
        save(new Vacancy(0, "Middle+ Java Developer", "Java разработчик уровня Middle+"));
        save(new Vacancy(0, "Senior Java Developer", "Java разработчик уровня Senior"));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.getAndIncrement());
        vacancies.putIfAbsent(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(
                        oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
