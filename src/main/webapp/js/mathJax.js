var math = null;
MathJax.Hub.queue.Push(function () {
    math = MathJax.Hub.getAllJax('MathOutput')[0];
});
window.updateMathMl = function (math) {
    document.getElementById('output').value = math.root.toMathML("");
};
function updateMath(TeX) {
    MathJax.Hub.queue.Push(['Text', math, '\\displaystyle{' + TeX + '}'], [updateMathMl, math]);
}