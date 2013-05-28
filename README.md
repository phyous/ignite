Ignite
======

Visualize current global twitter activity using Play & heatmap.js. Adapted from an example by [pa7](https://github.com/pa7/heatmap.js/blob/master/demo/maps_heatmap_layer/gmaps.html).

<center><img src="/images/example.png" height="500px"/></center>

## Usage

1- Install [play](http://www.playframework.com/documentation/2.0/Installing).

2- Add Twitter [application info](https://dev.twitter.com/apps) to conf/application.conf (see FILL_THIS_IN section)

3- Start the server:
> play run

4- Open 0.0.0.0:9000 in your browser. Keep refreshing, data will start to show up.

### TODO
- Make UI more awesome
- Rendering of map is a bit wonky when new data comes in.
- "Normalized" map mode is supposed to subtract the "mean" traffic using pre-computed models. This needs some tweaking.
