package com.lapha.logics;

public class Ref<T> {

  T ref;

  public Ref(T ref) {
    this.ref = ref;
  }

  public T getRef() {
    return ref;
  }

  public void setRef(T ref) {
    this.ref = ref;
  }
}
