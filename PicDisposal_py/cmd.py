import numpy as np
import numba as nb
from skimage import filters
import click
import time
import sys
import cv2
import os
import base64


@click.group()
def cmd():
    pass

@click.command(help="Binarize")
@click.option("--path", type=str, help="image path")
@click.option("--dir_save", type=str, help="directory storing disposed image")
@nb.jit()
def binarize(path, dir_save):
    st = time.time()
    img = cv2.imread(path, 0)
    _, dst = cv2.threshold(src=img, thresh=127, maxval=255, type=cv2.THRESH_BINARY)
    save_path = os.path.join(dir_save, "binary.jpg")
    # txt_path = os.path.join(dir_save, "binary.txt")
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@click.command(help="Pixelate")
@click.option("--path", type=str, help="image path")
@click.option("--dir_save", type=str, help="directory storing disposed image")
@nb.jit()
def pixelate(path, dir_save):
    st = time.time()
    img = cv2.imread(path)
    b, g, r = cv2.split(img)
    (h, w, _) = img.shape
    block_size = 30
    for y in range(0, h, block_size):
        for x in range(0, w, block_size):
            y_margin = y + block_size if y + block_size < h else h
            x_margin = x + block_size if x + block_size < w else w
            b[y: y_margin, x: x_margin] = square(b[y: y_margin, x: x_margin])
            g[y: y_margin, x: x_margin] = square(g[y: y_margin, x: x_margin])
            r[y: y_margin, x: x_margin] = square(r[y: y_margin, x: x_margin])
    ed = time.time()
    dst = cv2.merge([b, g, r])
    save_path = os.path.join(dir_save, "pixelate.jpg")
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@click.command(help="Segmentation")
@click.option("--path", type=str, help="image path")
@click.option("--k", type=str, help="the number of clusters")
@click.option("--dir_save", type=str, help="directory storing disposed image")
@nb.jit()
def segment(path, k, dir_save):
    st = time.time()
    k = int(k)
    img = cv2.imread(path)
    data = img.reshape((-1, 3))
    data = np.float32(data)
    k = int(k)
    criteria = (cv2.TERM_CRITERIA_EPS +
                cv2.TERM_CRITERIA_MAX_ITER, 10, 1.0)
    flags = cv2.KMEANS_RANDOM_CENTERS
    compactness, labels, centers = cv2.kmeans(data, k, None, criteria, 10, flags)
    centers = np.uint8(centers)
    res = centers[labels.flatten()]
    dst = res.reshape(img.shape)
    save_path = os.path.join(dir_save, "segment.jpg")
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@click.command(help="Sharpen")
@click.option("--path", type=str, help="image path")
@click.option("--dir_save", type=str, help="directory storing disposed image")
@nb.jit()
def sharpen(path, dir_save):
    st = time.time()
    img = cv2.imread(path)
    kernel = np.array([
        [0, -1, 0],
        [-1, 5, -1],
        [0, -1, 0]
    ])
    dst = cv2.filter2D(img, -1, kernel)
    save_path = os.path.join(dir_save, "sharpen.jpg")
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@click.command(help="Frosted glass")
@click.option("--path", type=str, help="image path")
@click.option("--dir_save", type=str, help="directory storing disposed image")
@nb.jit
def frosted_glass(path, dir_save):
    st = time.time()
    img = cv2.imread(path)
    (h, w, _) = img.shape
    dst = np.zeros_like(img)
    offset = 25

    for y in range(h - offset):
        for x in range(w - offset):
            rand = np.random.randint(0, offset)
            dst[y, x] = img[y + rand, x + rand]

    save_path = os.path.join(dir_save, "frosted_glass.jpg")
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@click.command(help="Emboss")
@click.option("--path", type=str, help="image path")
@click.option("--dir_save", type=str, help="directory storing disposed image")
@nb.jit()
def emboss(path, dir_save):
    st = time.time()
    img = cv2.imread(path)
    kernel = np.array([
        [-2, -1, 0],
        [-1, 1, 1],
        [0, 1, 2]
    ])
    dst = cv2.filter2D(img, -1, kernel)
    save_path = os.path.join(dir_save, "emboss.jpg")
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@click.command(help="Nostalgia")
@click.option("--path", type=str, help="image path")
@click.option("--dir_save", type=str, help="directory storing disposed image")
@nb.jit()
def nostalgia(path, dir_save):
    st = time.time()
    img = cv2.imread(path)
    b, g, r = cv2.split(img)
    (h, w, _) = img.shape
    b = np.sqrt(b) * 12 // 1
    b[b > 255] = 255
    b = b.astype(np.uint8)
    dst = cv2.merge([b, g, r])
    save_path = os.path.join(dir_save, "nostalgia.jpg")
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@click.command(help="Convex")
@click.option("--path", type=str, help="image path")
@click.option("--dir_save", type=str, help="directory storing disposed image")
@nb.jit
def convex(path, dir_save):
    st = time.time()
    img = cv2.imread(path)
    dst = loop_convex(img)
    dst = dst.astype(np.uint8)
    save_path = os.path.join(dir_save, "convex.jpg")
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@nb.jit
def loop_convex(img):
    (h, w, _) = img.shape
    dst = np.zeros(img.shape)
    c_y = h // 2
    c_x = w // 2
    r = min(c_x, c_y)
    for y in range(0, h):
        for x in range(0, w):
            dis = np.sqrt((y - c_y) ** 2 + (x - c_x) ** 2)
            if dis <= r:
                n_x = int(((x - c_x) * dis / r + c_x) // 1)
                n_y = int(((y - c_y) * dis / r + c_y) // 1)
                dst[y, x] = img[n_y, n_x]
            else:
                dst[y, x] = img[y, x]
    return dst


@click.command(help="Concave")
@click.option("--path", type=str, help="image path")
@click.option("--dir_save", type=str, help="directory storing disposed image")
def concave(path, dir_save):
    st = time.time()
    img = cv2.imread(path)
    dst = loop_concave(img)
    dst = dst.astype(np.uint8)
    save_path = os.path.join(dir_save, "concave.jpg")
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@nb.jit(nopython=True)
def loop_concave(img):
    (h, w, _) = img.shape
    dst = img.copy()
    c_y = h // 2
    c_x = w // 2
    r = np.sqrt(c_x ** 2 + c_y ** 2)
    for y in range(0, h):
        for x in range(0, w):
            dis = np.sqrt((y - c_y) ** 2 + (x - c_x) ** 2)
            n_x = int(((x - c_x) * dis / r + c_x) // 1)
            n_y = int(((y - c_y) * dis / r + c_y) // 1)
            dst[n_y, n_x] = img[y, x]
    return dst


@click.command(help="Cartoon")
@click.option("--path", type=str, help="image path")
@click.option("--dir_save", type=str, help="directory storing disposed image")
@nb.jit()
def cartoon(path, dir_save):
    st = time.time()
    save_path = os.path.join(dir_save, "cartoon.jpg")
    img = cv2.imread(path)
    num_bilateral = 3
    # use Gaussian pyramids to lower the sample
    img_color = np.copy(img)
    # bilateral filtering
    for i in range(num_bilateral):
        img_color = cv2.bilateralFilter(img_color, d=9, sigmaColor=9, sigmaSpace=3)
    # gray conversion
    img_gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
    # median filter
    img_blur = cv2.medianBlur(img_gray, 7)
    # edge detection and adaptive thresholding processing
    img_edge = cv2.adaptiveThreshold(img_blur, 255,
                                     cv2.ADAPTIVE_THRESH_MEAN_C,
                                     cv2.THRESH_BINARY,
                                     blockSize=25,
                                     C=2)
    # recover colorful image
    img_edge = cv2.cvtColor(img_edge, cv2.COLOR_GRAY2RGB)
    # AND operation
    dst = cv2.bitwise_and(img_color, img_edge)

    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@click.command(help="Look-up table")
@click.option("--path", type=str, help="image path")
@click.option("--approach", type=str, help="LUT type")
@click.option("--dir_save", type=str, help="directory storing disposed image")
def lut(path, approach, dir_save):
    st = time.time()
    approach_name = approach.split(".")[0]
    save_path = os.path.join(dir_save, f"lut_{approach_name}.jpg")
    lut_path = os.path.join(
        os.path.join(sys.path[0], "LUT"), approach)
    img = cv2.imread(path)
    dst = np.zeros(img.shape).astype(np.uint8)
    table = cv2.imread(lut_path)
    dst = lut_loop(img, table)
    cv2.imwrite(save_path, dst)
    print(time.time() - st)
    # cv2.namedWindow("img", 0)
    # cv2.resizeWindow("img", 500, 500)
    # cv2.moveWindow("img", 500, 200)
    # cv2.imshow("img", dst)
    # cv2.waitKey(0)


@nb.jit(nopython=True)
def lut_loop(img, table):
    dst = np.zeros(img.shape).astype(np.uint8)
    (h, w, _) = img.shape
    for y in range(h):
        for x in range(w):
            pixel = img[y, x]
            dst[y, x] = mapping(pixel, table)
    return dst


@nb.jit(nopython=True)
def mapping(pixel, table):
    (b, g, r) = pixel

    b_num = b / 255.0 * 63
    quad1_b_y = np.floor(b_num) // 8
    quad1_b_x = np.floor(b_num) - quad1_b_y * 8

    quad2_b_y = np.ceil(b_num) // 8
    quad2_b_x = np.ceil(b_num) - quad2_b_y * 8

    step_size = 0.5

    r_num = r / 255.0 * 63
    g_num = g / 255.0 * 63
    square_x = r_num + step_size
    square_y = g_num + step_size

    quad1_y = int(quad1_b_y * 64 + square_y)
    quad1_x = int(quad1_b_x * 64 + square_x)

    quad2_y = int(quad2_b_y * 64 + square_y)
    quad2_x = int(quad2_b_x * 64 + square_x)

    new_pixel = (table[quad1_y, quad1_x] * (b_num - np.floor(b_num)) + table[quad2_y, quad2_x] * (np.ceil(b_num) - b_num)) // 1

    return new_pixel


def square(block):
    values, counts = np.unique(block, return_counts=True)
    index = np.argmax(counts)
    val = values[index]
    return np.ones(block.shape, dtype=np.uint8) * val


def check_dir():
    dir_code = sys.path[0]
    print(dir_code)
    dir_save = os.path.join(dir_code, "out")
    if not os.path.exists(dir_save):
        os.mkdir(dir_save)
    return dir_save


cmd.add_command(binarize)
cmd.add_command(pixelate)
cmd.add_command(segment)
cmd.add_command(sharpen)
cmd.add_command(frosted_glass)
cmd.add_command(emboss)
cmd.add_command(nostalgia)
cmd.add_command(convex)
cmd.add_command(concave)
cmd.add_command(cartoon)
cmd.add_command(lut)

if __name__ == "__main__":
    cmd()