package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final MutableAclService aclService;

    @Override
    public void createDefaultPermissions(Object object) {
        MutableAcl acl = aclService.createAcl(new ObjectIdentityImpl(object));
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, new GrantedAuthoritySid("ROLE_USER"), true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, new GrantedAuthoritySid("ROLE_MANAGER"), true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, new GrantedAuthoritySid("ROLE_ADMIN"), true);
        aclService.updateAcl(acl);
    }

    @Override
    public void deletePermissions(Object object) {
        aclService.deleteAcl(new ObjectIdentityImpl(object), false);
    }
}
