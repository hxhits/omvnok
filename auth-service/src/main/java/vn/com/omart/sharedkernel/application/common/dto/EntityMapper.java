package vn.com.omart.sharedkernel.application.common.dto;

public interface EntityMapper<D, E> {
    D map(E entity);

    void map(E gift, D dto);
}

