package com.example.reshare.presentation.features.sideBar.myImpact.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.reshare.R
import com.example.reshare.domain.model.Product
import com.example.reshare.domain.model.Requests
import com.example.reshare.presentation.utils.capitalizeFirst
import com.example.reshare.presentation.utils.getBadgeStyle

@Composable
fun MyRequestsScreen(
    products: List<Requests>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(products) {
            MyRequestsItem(
                requests = it,
                rating = 5.0f
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun MyRequestsItem(
    requests: Requests,
    rating: Float?
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(110.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row {
            Box(modifier = Modifier
                .width(110.dp)
                .fillMaxHeight()){
                // Left Image
                AsyncImage(
                    model = requests.productId.images[0],
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                    placeholder = painterResource(R.drawable.img),
                    error = painterResource(R.drawable.img)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .size(24.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.StarOutline,
                        contentDescription = "Favorite",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .padding(end = 16.dp),

                    ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val statusStyle = getStatusBadgeStyle(requests.status)
                        Text(
                            text = requests.status.capitalizeFirst(),
                            color = statusStyle.textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            style = TextStyle(lineHeight = 12.sp),
                            modifier = Modifier
                                .background(statusStyle.backgroundColor, shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        if (requests.productId.originalPrice != null) {
                            Text(
                                text = "${requests.productId.originalPrice} k",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                style = TextStyle(lineHeight = 12.sp),
                                modifier = Modifier
                                    .background(
                                        Color.Black.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))
                        val style = getBadgeStyle(requests.productId.tag)
                        Text(
                            text = requests.productId.tag.capitalizeFirst(),
                            color = style.textColor,
                            fontSize = 12.sp,
                            style = TextStyle(lineHeight = 12.sp),
                            modifier = Modifier
                                .background(style.backgroundColor, shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    // Title
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = requests.productId.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    //Seller & rating
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = requests.productId.createdBy.profilePic,
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.user),
                            error = painterResource(R.drawable.user)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = requests.productId.createdBy.firstName, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        if (rating != null) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(text = rating.toString(), fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Pickup Time: ${requests.pickupTime}",
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

data class BadgeStyle(val textColor: Color, val backgroundColor: Color)

fun getStatusBadgeStyle(status: String): BadgeStyle {
    return when (status.lowercase()) {
        "accepted" -> BadgeStyle(Color.White, Color(0xFF4CAF50)) // Green
        "pending" -> BadgeStyle(Color.Black, Color(0xFFFFC107)) // Amber
        "rejected" -> BadgeStyle(Color.White, Color(0xFFF44336)) // Red
        else -> BadgeStyle(Color.White, Color.Gray)
    }
}
