#include "library.h"
#include <iostream>
#include <algorithm>
#include <nana/gui.hpp>
#include <nana/gui/widgets/button.hpp>
#include <nana/gui/widgets/label.hpp>
#include <nana/gui/widgets/textbox.hpp>
#include <nana/gui/widgets/checkbox.hpp>

using namespace std;

vector<double> *getDoubleArray(bool &order) {
    auto resArray = new vector<double>();
    using namespace nana;

    form fm;

    label lab{fm, "Data list (separated by space)"};
    lab.format(true);

    textbox textbox{fm, "asd"};
    checkbox checkbox{fm, "Sort descending"};

    button btn{fm, "Ok"};
    btn.events().click([&fm, &textbox, &resArray, &order, &checkbox] {
        order = checkbox.checked();
        stringstream ss;
        auto data = vector<string>();
        ss.str(textbox.text());
        string s;
        while (getline(ss, s, ' ')) {
            data.push_back(s);
        }

        for (const auto &item: data) {
            resArray->push_back(stod(item));
        }
        fm.close();

    });

    fm.div("vert <><<><weight=80% text><>><<><weight=80% textbox><>><><<><weight=80% checkBox><>><><weight=24<><button><>><>");
    fm["text"] << lab;
    fm["button"] << btn;
    fm["textbox"] << textbox;
    fm["checkBox"] << checkbox;
    fm.collocate();

    fm.show();

    exec();
    return resArray;
}

JNIEXPORT jobjectArray JNICALL Java_pl_edu_pwr_lab11_Sorting_sort01
        (JNIEnv *env, jobject obj, jobjectArray array, jobject order) {
    auto len = env->GetArrayLength(array);
    bool ord = env->
            CallBooleanMethod(order, env->
                GetMethodID(env->FindClass("java/lang/Boolean"), "booleanValue", "()Z"));
    auto *newTab = new std::vector<double>;
    auto doubleClass = env->FindClass("java/lang/Double");
    for (int i = 0; i < len; i++) {
        auto objElement = env->GetObjectArrayElement(array, i);
        double val = env->CallDoubleMethod(objElement, env->GetMethodID(doubleClass, "doubleValue", "()D"));
        newTab->push_back(val);
    }

    if (ord) std::sort(newTab->begin(), newTab->end(), std::greater<>());
    else std::sort(newTab->begin(), newTab->end());

    auto doubleConstructor = env->GetMethodID(doubleClass, "<init>", "(D)V");

    for (int i = 0; i < len; i++) {
        double element = newTab->at(i);
        auto doubleObject = env->NewObject(doubleClass, doubleConstructor, element);
        env->SetObjectArrayElement(array, i, doubleObject);
    }

    return array;
}


JNIEXPORT jobjectArray JNICALL Java_pl_edu_pwr_lab11_Sorting_sort02
        (JNIEnv *env, jobject obj, jobjectArray array) {
    auto orderFieldId = env->GetFieldID(env->GetObjectClass(obj), "order", "Ljava/lang/Boolean;");
    auto orderObj = env->GetObjectField(obj, orderFieldId);
    return Java_pl_edu_pwr_lab11_Sorting_sort01(env, obj, array, orderObj);
}

JNIEXPORT void JNICALL Java_pl_edu_pwr_lab11_Sorting_sort03
        (JNIEnv *env, jobject obj) {
    bool order;
    auto inputArray = getDoubleArray(order);

    auto doubleClass = env->FindClass("java/lang/Double");
    auto doubleConstructor = env->GetMethodID(doubleClass, "<init>", "(D)V");

    auto booleanClass = env->FindClass("java/lang/Boolean");
    auto booleanConstructor = env->GetMethodID(booleanClass, "<init>", "(Z)V");

    auto aField = env->GetFieldID(env->GetObjectClass(obj), "a", "[Ljava/lang/Double;");
    auto bField = env->GetFieldID(env->GetObjectClass(obj), "b", "[Ljava/lang/Double;");
    auto orderField = env->GetFieldID(env->GetObjectClass(obj), "order", "Ljava/lang/Boolean;");

    auto aObjectArray = env->NewObjectArray((jsize) inputArray->size(), doubleClass, NULL);
    auto bObjectArray = env->NewObjectArray((jsize) inputArray->size(), doubleClass, NULL);

    for (int i = 0; i < inputArray->size(); i++) {
        double element = inputArray->at(i);
        auto doubleObject = env->NewObject(doubleClass, doubleConstructor, element);
        env->SetObjectArrayElement(aObjectArray, i, doubleObject);
    }

    if (order) std::sort(inputArray->begin(), inputArray->end(), std::greater<>());
    else std::sort(inputArray->begin(), inputArray->end());

    for (int i = 0; i < inputArray->size(); i++) {
        double element = inputArray->at(i);
        auto doubleObject = env->NewObject(doubleClass, doubleConstructor, element);
        env->SetObjectArrayElement(bObjectArray, i, doubleObject);
    }


    auto booleanObject = env->NewObject(booleanClass, booleanConstructor, order);
    env->SetObjectField(obj, orderField, booleanObject);

    env->SetObjectField(obj, aField, aObjectArray);
    env->SetObjectField(obj, bField, bObjectArray);
}

