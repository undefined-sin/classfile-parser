package net.classfileparser;

class ClassMemberRef {
    private short access_flags;
    private short name_index;
    private short descriptor_index;
    private int numberOfAttributes;

    public ClassMemberRef(short accessFlags, short nameIndex, short descriptorIndex) {
        this.access_flags = accessFlags;
        this.name_index = nameIndex;
        this.descriptor_index = descriptorIndex;
    }

    public short getAccess_flags() {
        return access_flags;
    }

    public void setAccess_flags(short access_flags) {
        this.access_flags = access_flags;
    }

    public short getNameIndex() {
        return name_index;
    }

    public void setName_index(short name_index) {
        this.name_index = name_index;
    }

    public short getDescriptor_index() {
        return descriptor_index;
    }

    public void setDescriptor_index(short descriptor_index) {
        this.descriptor_index = descriptor_index;
    }

    public boolean isPublic() {
        return MemberAccess.hasAccess(access_flags, MemberAccess.ACC_PUBLIC);
    }

    public boolean isStatic() {
        return MemberAccess.hasAccess(access_flags, MemberAccess.ACC_STATIC);
    }

    public short getName_index() {
        return name_index;
    }

    public int getNumberOfAttributes() {
        return numberOfAttributes;
    }

    public void setNumberOfAttributes(int numberOfAttributes) {
        this.numberOfAttributes = numberOfAttributes;
    }
}
