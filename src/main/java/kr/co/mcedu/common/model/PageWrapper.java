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
    private List<T> list;
    private Long total;
    private Long pageSize;
    private Long offset;
    private Long pageNum;
    private Integer totalPage;
    public PageWrapper(Collection<T> collection, long total, long pageSize, final long offset) {
        this.list = new ArrayList<>(collection);
        this.total = total;
        this.pageSize = pageSize;
        this.offset = offset;
        this.pageNum = offset / pageSize;
        this.totalPage = (int) Math.ceil((double) total / (double) pageSize);
    }

    public <R> PageWrapper<R> change(Function<T, R> changer) {
        List<R> collect = this.list.stream().map(changer).collect(Collectors.toList());
        return new PageWrapper<>(collect, total, pageSize, offset);
    }

    public static <T> PageWrapper<T> of(final QueryResults<T> queryResults) {
        return new PageWrapper<>(queryResults.getResults(), queryResults.getTotal(), queryResults.getLimit(), queryResults.getOffset());
    }
}
