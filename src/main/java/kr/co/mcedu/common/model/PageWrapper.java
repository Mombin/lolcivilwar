package kr.co.mcedu.common.model;

import com.querydsl.core.QueryResults;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class PageWrapper<T> {
    private List<T> data;
    private Long total;
    private Long pageSize;
    public PageWrapper(Collection<T> collection, long total, long pageSize) {
        this.data = new ArrayList<>(collection);
        this.total = total;
        this.pageSize = pageSize;
    }

    public <R> PageWrapper<R> change(Function<T, R> changer) {
        List<R> collect = this.data.stream().map(changer).collect(Collectors.toList());
        return new PageWrapper<>(collect, total, pageSize);
    }

    public static <T> PageWrapper<T> of(final QueryResults<T> queryResults) {
        return new PageWrapper<>(queryResults.getResults(), queryResults.getTotal(), queryResults.getLimit());
    }
}
