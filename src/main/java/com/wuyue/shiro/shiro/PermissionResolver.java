package com.wuyue.shiro.shiro;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * 资源权限解析器
 *
 * @author wuyue
 * @since 1.0, 2019-09-07
 */
public interface PermissionResolver {

    /**
     * 解析资源
     *
     * @return 资源的权限表示字符串
     */
    String resolve();

    /**
     * 批量解析资源
     */
    static List<String> resolve(List<PermissionResolver> list) {
        return Optional.ofNullable(list).map(obj -> obj.stream().map(PermissionResolver::resolve).collect(toList()))
                .orElse(Collections.emptyList());
    }

}
