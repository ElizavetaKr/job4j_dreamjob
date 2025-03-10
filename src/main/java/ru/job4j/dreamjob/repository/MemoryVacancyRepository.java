package ru.job4j.dreamjob.repository;

import com.google.errorprone.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Хранит вакансии
 */
@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "описание...", true, 1, 0));
        save(new Vacancy(0, "Junior Java Developer", "описание...", true, 2, 0));
        save(new Vacancy(0, "Junior+ Java Developer", "описание...", true, 3, 0));
        save(new Vacancy(0, "Middle Java Developer", "описание...", true, 1, 0));
        save(new Vacancy(0, "Middle+ Java Developer", "описание...", true, 2, 0));
        save(new Vacancy(0, "Senior Java Developer", "описание...", true, 3, 0));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(),
                        vacancy.getDescription(), vacancy.getVisible(),
                        vacancy.getCityId(), vacancy.getFileId())) != null;
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
