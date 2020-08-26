# coding=utf-8
'''
@Description: 示例所用的通用工具类
@Author: superz
@Date: 2020-01-02 22:23:47
@LastEditTime : 2020-01-03 23:37:42
'''


class Student():
    """
    在Python中可以使用class关键字定义类
    """

    def __init__(self, id, name):
        self._id = id
        self._name = name

    def __repr__(self):
        return "id="+self._id+",name="+self._name

    @property
    def id(self):
        return self._id

    @id.setter
    def id(self, id):
        self._id = id

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, name):
        self._name = name

    """
    静态方法和类方法都是通过给类发消息来调用的
    """
    @staticmethod
    def static_method(info):
        print(f"这是静态方法，传入的提示信息是：{info}")

    @classmethod
    def class_method(cls, info):
        """
        Python可以在类中定义类方法，类方法的第一个参数约定名为cls，它代表的是当前类相关的信息的对象（类本身也是一个对象，有的地方也称之为类的元数据对象），通过这个参数我们可以获取和类相关的信息并且可以创建出类的对象
        """
        print(f"这是类方法，传入的提示信息是：{info}")


class undergraduate(Student):
    """
    继承相关
    """

    def __init__(self, id, name, note):
        super().__init__(id, name)
        self._note = note

    @property
    def note(self):
        return self._note

    @note.setter
    def note(self, note):
        self._note = note

    def detailinfo(self):
        print(f"姓名：{self._name}，班级：{self._id}，详细信息：{self._note}")


class VisibilityDemo():
    """
    可见性测试类
    在Python中，属性和方法的访问权限只有两种，也就是公开的和私有的，如果希望属性是私有的，在给属性命名时可以用两个下划线作为开头，下面的代码可以验证这一点
    在实际开发中，并不建议将属性设置为私有的，因为这会导致子类无法访问。所以大多数Python程序员会遵循一种命名惯例就是让属性名以单下划线开头来表示属性是受保护的，本类之外的代码在访问这样的属性时应该要保持慎重。这种做法并不是语法上的规则，单下划线开头的属性和方法外界仍然是可以访问的，所以更多的时候它是一种暗示或隐喻
    """

    def __init__(self, foo):
        self.__foo = foo

    def __bar(self):
        print(self.__foo+"__bar")


class PropertyDemo():
    """
    @property装饰器
    之前的建议是将属性命名以单下划线开头，通过这种方式来暗示属性是受保护的，不建议外界直接访问，那么如果想访问属性可以通过属性的getter（访问器）和setter（修改器）方法进行对应的操作。如果要做到这点，就可以考虑使用@property包装器来包装getter和setter方法，使得对属性的访问既安全又方便
    """

    def __init__(self, name, age):
        self._name = name
        self._age = age

    # 访问器-getter方法
    @property
    def name(self):
        return self._name

    @property
    def age(self):
        return self._age

    # 修改器-setter方法
    @age.setter
    def age(self, age):
        self._age = age

    def play(self):
        if self._age <= 16:
            print('%s正在玩飞行棋.' % self._name)
        else:
            print('%s正在玩斗地主.' % self._name)


class CustomException(Exception):
    """
    自定义异常类型，只需要从Exception类继承即可(直接或间接)
    """
    pass


"""
Python解释器在导入这个模块时会执行那些可执行的代码，如果不想在导入模块的时候执行这些代码，可将这些代码放入以下的条件中，这样的话除非直接运行模块，不然if条件下的这些代码不会执行的，因为只有直接执行的模块的名称才是"__main__"
todo 此项规则要单独出来
"""
if __name__ == "__main__":
    xiaoming = Student("001", "xiaoming")
    # 静态方法和类方法都是通过给类发消息来调用的
    Student.static_method("msg")
    Student.class_method("msg")

    lisi = undergraduate("lisi", 27, "这是一个测试信息")
    lisi.detailinfo()
    lisi.note = "这是测试信息2"
    lisi.detailinfo()

    demo = VisibilityDemo("test")
    # AttributeError: 'VisibilityDemo' object has no attribute '__bar'
    # demo.__bar()
    # AttributeError: 'VisibilityDemo' object has no attribute '__foo'
    # print(demo.__foo)
    # Python并没有从语法上严格保证私有属性或方法的私密性，它只是给私有的属性和方法换了一个名字来妨碍对它们的访问，事实上如果你知道更换名字的规则仍然可以访问到它们，下面的代码就可以验证这一点。之所以这样设定，可以用这样一句名言加以解释，就是"We are all consenting adults here"。因为绝大多数程序员都认为开放比封闭要好，而且程序员要自己为自己的行为负责
    demo._VisibilityDemo__bar()
    print(demo._VisibilityDemo__foo)

    person = PropertyDemo("zhangsan", 27)
    person.play()
    person.age = 15
    person.play()
    # person.name = "lisi" # AttributeError: can't set attribute
