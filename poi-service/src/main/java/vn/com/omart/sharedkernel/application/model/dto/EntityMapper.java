package vn.com.omart.sharedkernel.application.model.dto;

public interface EntityMapper<D, E> {
    D map(E entity);

    void map(E entity, D dto);
}

